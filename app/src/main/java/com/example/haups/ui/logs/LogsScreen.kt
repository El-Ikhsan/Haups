package com.example.haups.ui.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haups.R

data class FeedLog(
    val id: Int,
    val time: String,
    val date: String,
    val status: String,
    val isSuccess: Boolean
)

@Composable
fun LogsScreen() {
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }

    val logs = remember {
        listOf(
            FeedLog(1, "08:00", "28 Nov 2025", "Pakan Pagi", true),
            FeedLog(2, "12:30", "28 Nov 2025", "Pakan Siang", true),
            FeedLog(3, "18:00", "28 Nov 2025", "Pakan Malam", false),
            FeedLog(4, "08:00", "27 Nov 2025", "Pakan Pagi", true),
            FeedLog(5, "12:30", "27 Nov 2025", "Pakan Siang", true),
            FeedLog(6, "18:00", "27 Nov 2025", "Pakan Malam", true),
            FeedLog(7, "08:00", "26 Nov 2025", "Pakan Pagi", false),
            FeedLog(8, "12:30", "26 Nov 2025", "Pakan Siang", true)
        )
    }

    val filteredLogs = logs.filter { log ->
        selectedFilter == null || log.status == selectedFilter
    }

    val filterOptions = listOf("Pakan Pagi", "Pakan Siang", "Pakan Malam")

    Scaffold(containerColor = Color.Transparent) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(bottom = paddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Header "Riwayat Pakan" + 2. Icon Filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Riwayat Pakan",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.font_primer)
                )

                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tab_logs),
                        contentDescription = "Filter",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { showFilterMenu = !showFilterMenu },
                        tint = colorResource(R.color.cyan_primer)
                    )

                    // Dropdown menu untuk filter
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Semua") },
                            onClick = {
                                selectedFilter = null
                                showFilterMenu = false
                            }
                        )
                        filterOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedFilter = option
                                    showFilterMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // 3. Box Filter yang Dipilih dengan Tanda X
            if (selectedFilter != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter: $selectedFilter",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "✕",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .clickable { selectedFilter = null }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // 4. Box Data Log Pakan
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filteredLogs) { log ->
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (log.isSuccess)
                                colorResource(R.color.grey_second)
                            else
                                colorResource(R.color.red_primer)
                        ),
                        border = if (log.isSuccess)
                            BorderStroke(2.dp, colorResource(R.color.cyan_primer))
                        else
                            null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Status indicator
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = if (log.isSuccess) colorResource(R.color.cyan_primer)
                                        else colorResource(R.color.dark_primer),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // Time
                            Text(
                                text = log.time,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.font_primer),
                                modifier = Modifier.width(80.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Log details
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = log.status,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = colorResource(R.color.font_primer)
                                )
                                Text(
                                    text = log.date,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (log.isSuccess) colorResource(R.color.grey_primer)
                                    else colorResource(R.color.font_primer).copy(alpha = 0.8f)
                                )
                                Text(
                                    text = if (log.isSuccess) "Berhasil" else "Gagal",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (log.isSuccess) colorResource(R.color.cyan_primer)
                                    else colorResource(R.color.font_primer),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                if (filteredLogs.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Tidak ada data log pakan",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogsScreenPreview() {
    LogsScreen()
}
