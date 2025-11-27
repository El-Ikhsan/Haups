package com.example.haups.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.ButtonDefaults
import com.example.haups.R
import com.example.haups.ui.theme.HaupsTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class ConnectionStatus(val label: String) {
    object Online : ConnectionStatus("Online")
    object Offline : ConnectionStatus("Offline")
}

data class FeedLogEntry(val timestamp: String, val result: String)

@Composable
fun StatusCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    iconRes: Int? = null,
    backgroundColor: Color = colorResource(R.color.blue_primer) // default, bisa diatur
) {
    Card(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
        ) {
            // top: place icon circle at top-left (if provided). Title is on its own line under the icon
            Box(modifier = Modifier.fillMaxWidth()) {
                if (iconRes != null) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(color = colorResource(id = R.color.dark_primer), shape = CircleShape)
                            .align(Alignment.TopStart),
                        contentAlignment = Alignment.Center
                    ) {
                        // Keep icon smaller than the circle so it has padding and is visible
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            tint = colorResource(R.color.white),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // title on its own line (will not be on the same row as the icon)
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = colorResource(R.color.dark_primer),
                ),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // value centered under the title
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = value, style = MaterialTheme.typography.titleMedium.copy(
                    color = colorResource(R.color.dark_primer),
                ))
            }
        }
    }
}

@Composable
fun HomeScreen(
    // Assumption: device is reachable at this base URL; override for testing or different devices
    feedEndpoint: String = "http://192.168.4.1/feed"
) {
    // UI state
    val remainingFeed = remember { mutableStateOf(1200) } // grams, example initial
    val lastFeedTime = remember { mutableStateOf("-") }
    val connection = remember { mutableStateOf<ConnectionStatus>(ConnectionStatus.Offline) }
    val feedLogs = remember { mutableStateListOf<FeedLogEntry>() }
    val isFeeding = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun nowTimestamp(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return fmt.format(Date())
    }

    fun addLog(result: String) {
        feedLogs.add(0, FeedLogEntry(nowTimestamp(), result))
    }

    fun probeConnectionAndUpdate() {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val url = URL(feedEndpoint)
                    val conn = (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = "HEAD"
                        connectTimeout = 2000
                        readTimeout = 2000
                        instanceFollowRedirects = false
                    }
                    val code = conn.responseCode
                    if (code in 200..399) {
                        connection.value = ConnectionStatus.Online
                    } else connection.value = ConnectionStatus.Offline
                    conn.disconnect()
                } catch (_: Exception) {
                    connection.value = ConnectionStatus.Offline
                }
            }
        }
    }

    fun postFeed() {
        scope.launch {
            isFeeding.value = true
            addLog("Requesting feed...")
            val resultLabel = withContext(Dispatchers.IO) {
                try {
                    val url = URL(feedEndpoint)
                    val conn = (url.openConnection() as HttpURLConnection).apply {
                        requestMethod = "POST"
                        doOutput = true
                        connectTimeout = 5000
                        readTimeout = 5000
                        setRequestProperty("Content-Type", "application/json")
                    }
                    // simple empty JSON body; device may expect different format — adapt if needed
                    OutputStreamWriter(conn.outputStream).use { it.write("{}") }
                    val code = conn.responseCode
                    try { conn.inputStream.close() } catch (_: Exception) {}
                    conn.disconnect()
                    when (code) {
                        200 -> "Berhasil"
                        202 -> "Ditunda"
                        else -> "Gagal (code=$code)"
                    }
                } catch (e: Exception) {
                    "Gagal: ${e.localizedMessage ?: e.javaClass.simpleName}"
                }
            }

            // update UI state based on result
            addLog(resultLabel)
            if (resultLabel.startsWith("Berhasil")) {
                lastFeedTime.value = nowTimestamp()
                remainingFeed.value = (remainingFeed.value - 50).coerceAtLeast(0) // contoh pengurangan
                connection.value = ConnectionStatus.Online
            } else {
                // if failure, probe connection to set status
                probeConnectionAndUpdate()
            }

            isFeeding.value = false
        }
    }

    // initial probe
    probeConnectionAndUpdate()

    Scaffold(containerColor = Color.Transparent) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero section: full-width, fit background, no padding on sides and top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background image - ganti ic_launcher_background dengan gambar yang Anda inginkan
                Image(
                    painter = painterResource(id = R.drawable.dark_anime),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Semi-transparent overlay untuk meningkatkan keterbacaan teks
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                Text(
                    text = "sistem kontrol pakan ikan otomatis",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White, // Mengubah warna teks menjadi putih agar kontras dengan background
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            // Content with horizontal padding
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)


            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Status cards row
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatusCard(
                        modifier = Modifier.weight(1f),
                        title = "sisa pakan",
                        value = "${remainingFeed.value} g",
                        iconRes = R.drawable.ic_tab_home,
                        backgroundColor = colorResource(R.color.s1)
                    )
                    StatusCard(
                        modifier = Modifier.weight(1f),
                        title = "pakan terakhir",
                        value = lastFeedTime.value,
                        iconRes = R.drawable.ic_tab_schedule,
                        backgroundColor = colorResource(R.color.s2)
                    )
                    StatusCard(
                        modifier = Modifier.weight(1f),
                        title = "koneksi",
                        value = connection.value.label,
                        iconRes = R.drawable.ic_tab_settings,
                        backgroundColor = colorResource(R.color.s3)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                // Single full-width Feed Now button with horizontal padding 30.dp
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = { postFeed() },
                        enabled = !isFeeding.value,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            // Ganti Color.Blue dengan warna yang Anda inginkan
                            // Bisa pakai colorResource(R.color.nama_warna) atau Color(0xFF...)
                            containerColor = colorResource(id = R.color.cyan_primer),

                            // Warna Teks / Icon di dalamnya
                            contentColor = Color.White,

                            // (Opsional) Warna saat tombol disabled (saat loading)
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)

                    ) {
                        if (isFeeding.value) {
                            CircularProgressIndicator(modifier = Modifier.size(25.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Loading")
                        } else {
                            Text(
                                text = "Beri Pakan Sekarang",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = colorResource(R.color.dark_primer), // Mengubah warna teks menjadi putih agar kontras dengan background
                                    fontWeight = FontWeight.SemiBold
                                ),
                                textAlign = TextAlign.Center)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "History Feed", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .padding(horizontal = 16.dp)) {
                items(feedLogs) { log ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.cyan_primer)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = log.timestamp, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = log.result, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HaupsTheme {
        HomeScreen(feedEndpoint = "http://example.local/feed")
    }
}
