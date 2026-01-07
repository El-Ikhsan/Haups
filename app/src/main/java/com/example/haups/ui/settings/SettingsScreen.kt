package com.example.haups.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.haups.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen() {
    var isOnline by remember { mutableStateOf(false) }
    var ipAddress by remember { mutableStateOf("192.168.1.100") }
    var showIpDialog by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color.Transparent) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(bottom = paddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Header "Pengaturan Alat"
            Text(
                text = "Pengaturan Alat",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.font_primer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            // 2. Lingkaran besar "Cari" untuk scan ESP32
            Box(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(end = 65.dp)
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.dark_primer))
                    .border(5.dp, colorResource(R.color.cyan_primer), CircleShape)
                    .clickable {
                        // Fungsi scan perangkat ESP32
                        isOnline = true
                    }
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CARI",
                    color = colorResource(R.color.font_primer),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 3. Lingkaran kecil "Tes Koneksi" (geser ke kanan)
            Box(
                modifier = Modifier
                    .padding(start = 120.dp)
                    .size(95.dp)
                    .clip(CircleShape)
                    .background(colorResource(R.color.dark_primer))
                    .border(2.5.dp, colorResource(R.color.yellow_primer), CircleShape)
                    .clickable {
                        // Fungsi tes koneksi ESP32
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TES\nKONEKSI",
                    color = colorResource(R.color.font_primer),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            }

            // 4. Status Online/Offline
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (isOnline) colorResource(R.color.cyan_primer) else colorResource(R.color.red_primer))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isOnline) "Online" else "Offline",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isOnline) colorResource(R.color.cyan_primer) else colorResource(R.color.red_primer)
                )
            }

            // 5. Box Detail Informasi
            Card(
                border = BorderStroke(
                    width = 2.dp,
                    colorResource(id = R.color.cyan_primer)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.dark_primer)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Baris 1: ESP-32
                    Text(
                        text = "ESP-32",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.font_primer)
                    )

                    // Baris 2: IP Address dengan icon edit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ipAddress,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colorResource(R.color.font_primer),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tab_settings),
                            contentDescription = "Edit IP",
                            tint = colorResource(R.color.cyan_primer),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    showIpDialog = true
                                }
                        )
                    }

                    // Baris 3: Shimarin Dev v.1.0 dengan tanggal di bawahnya
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Shimarin Dev v.1.0",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.font_primer)
                        )
                        Text(
                            text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                            style = MaterialTheme.typography.bodySmall,
                            color = colorResource(R.color.grey_primer)
                        )
                    }

                    // Baris 4: MIT License dengan icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚖️",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "MIT License",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorResource(R.color.font_primer)
                        )
                    }
                }
            }
        }
    }

    // IP Address Change Popup
    ChangeIpAddressPopup(
        showDialog = showIpDialog,
        currentIp = ipAddress,
        onDismiss = { showIpDialog = false },
        onConfirm = { newIp ->
            ipAddress = newIp
        }
    )
}

@Composable
fun ChangeIpAddressPopup(
    showDialog: Boolean,
    currentIp: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var ipAddress by remember { mutableStateOf(currentIp) }
    var isValidIp by remember { mutableStateOf(true) }

    fun validateIpAddress(ip: String): Boolean {
        if (ip.isBlank()) return false
        val parts = ip.split(".")
        if (parts.size != 4) return false
        return parts.all { part ->
            try {
                val num = part.toInt()
                num in 0..255
            } catch (_: NumberFormatException) {
                false
            }
        }
    }

    LaunchedEffect(currentIp) {
        ipAddress = currentIp
        isValidIp = validateIpAddress(currentIp)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tab_settings),
                        contentDescription = "Settings Icon",
                        tint = colorResource(R.color.cyan_primer),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "Ganti Alamat IP",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.font_primer),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        "Masukkan alamat IP baru untuk ESP32:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(R.color.font_primer),
                        fontWeight = FontWeight.Medium
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.grey_second)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                value = ipAddress,
                                onValueChange = {
                                    ipAddress = it
                                    isValidIp = validateIpAddress(it)
                                },
                                label = {
                                    Text(
                                        "IP Address",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                },
                                placeholder = {
                                    Text(
                                        "192.168.1.100",
                                        color = colorResource(R.color.grey_primer)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = !isValidIp,
                                textStyle = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_tab_home),
                                        contentDescription = "IP Icon",
                                        tint = if (isValidIp) colorResource(R.color.cyan_primer)
                                              else colorResource(R.color.red_primer),
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = colorResource(R.color.cyan_primer),
                                    focusedLabelColor = colorResource(R.color.cyan_primer),
                                    errorBorderColor = colorResource(R.color.red_primer),
                                    errorLabelColor = colorResource(R.color.red_primer),
                                    unfocusedBorderColor = colorResource(R.color.white_outline_primer),
                                    unfocusedLabelColor = colorResource(R.color.grey_primer),
                                    focusedLeadingIconColor = colorResource(R.color.cyan_primer),
                                    unfocusedLeadingIconColor = colorResource(R.color.grey_primer)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            if (!isValidIp && ipAddress.isNotBlank()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorResource(R.color.red_primer).copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_tab_home),
                                            contentDescription = "Error",
                                            tint = colorResource(R.color.red_primer),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Format IP tidak valid\n(contoh: 192.168.1.100)",
                                            color = colorResource(R.color.red_primer),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            } else if (isValidIp && ipAddress.isNotBlank()) {
                                // Preview IP display
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorResource(R.color.cyan_primer).copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_tab_home),
                                            contentDescription = "Success",
                                            tint = colorResource(R.color.cyan_primer),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "IP Address Valid: $ipAddress",
                                            color = colorResource(R.color.cyan_primer),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // Current IP info
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(R.color.white_outline_primer).copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_tab_settings),
                                        contentDescription = "Current",
                                        tint = colorResource(R.color.grey_primer),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "IP Saat Ini:",
                                            color = colorResource(R.color.grey_primer),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            text = currentIp,
                                            color = colorResource(R.color.font_primer),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isValidIp && ipAddress.isNotBlank()) {
                            onConfirm(ipAddress)
                            onDismiss()
                        }
                    },
                    enabled = isValidIp && ipAddress.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.cyan_primer),
                        contentColor = colorResource(R.color.dark_primer),
                        disabledContainerColor = colorResource(R.color.grey_primer),
                        disabledContentColor = colorResource(R.color.white_outline_primer)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tab_settings),
                        contentDescription = "Save",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Simpan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(R.color.font_primer),
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colorResource(R.color.white_outline_primer)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        "Batal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            containerColor = colorResource(R.color.dark_primer),
            titleContentColor = colorResource(R.color.font_primer),
            textContentColor = colorResource(R.color.font_primer),
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
