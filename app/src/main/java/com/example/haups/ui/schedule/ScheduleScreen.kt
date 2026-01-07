package com.example.haups.ui.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.colorResource
import com.example.haups.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// Model Data
data class FeedingSchedule(
    val id: Int,
    val time: LocalTime,
    val isActive: Boolean,
    val isSelected: Boolean = false
)

@Composable
fun ScheduleScreen() {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    val selectedTime = LocalTime.now()
    var showAddDialog by remember { mutableStateOf(false) }

    var schedules by remember {
        mutableStateOf(listOf(
            FeedingSchedule(1, LocalTime.of(8, 0), true),
            FeedingSchedule(2, LocalTime.of(12, 30), false),
            FeedingSchedule(3, LocalTime.of(18, 0), true)
        ))
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(bottom = paddingValues.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header - Fixed
            Text(
                text = "Jadwal Pakan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.font_primer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Start
            )

            // Calendar Box - Fixed
            Card(
                border = BorderStroke(
                    width = 4.dp,
                    colorResource(id = R.color.white_outline_primer)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.dark_primer)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tab_schedule),
                        contentDescription = "Calendar",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentDate.year}",
                        style = MaterialTheme.typography.titleLarge,
                        color = colorResource(R.color.font_primer),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Days of Week - Fixed
            val startOfWeek = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(7) { index ->
                    val date = startOfWeek.plusDays(index.toLong())
                    val isToday = date == LocalDate.now()

                    Card(
                        border = BorderStroke(
                            width = 4.dp,
                            color = if (isToday) colorResource(id = R.color.cyan_primer)
                            else colorResource(R.color.white_outline_primer)
                        ),
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isToday) colorResource(R.color.cyan_primer)
                            else colorResource(R.color.dark_primer)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isToday) colorResource(R.color.dark_primer)
                                else colorResource(R.color.font_primer)
                            )
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isToday) colorResource(R.color.dark_primer)
                                else colorResource(R.color.font_primer)
                            )
                        }
                    }
                }
            }

            // Time Display - Fixed
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = MaterialTheme.typography.displayLarge.fontSize * 1.5),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.font_primer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Add and Delete Buttons - Fixed
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        showAddDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.cyan_primer),
                        contentColor = colorResource(id = R.color.dark_primer)
                    )
                ) {
                    Text("Tambah Jadwal")
                }

                Button(
                    onClick = {
                        schedules = schedules.filter { !it.isSelected }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.red_primer),
                        contentColor = colorResource(id = R.color.dark_primer)
                    )
                ) {
                    Text("Hapus")
                }
            }

            // Schedule List - Scrollable LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                items(schedules) { schedule ->
                    ScheduleItem(
                        schedule = schedule,
                        onToggleActive = {
                            schedules = schedules.map {
                                if (it.id == schedule.id) it.copy(isActive = !it.isActive)
                                else it
                            }
                        },
                        onToggleSelect = {
                            schedules = schedules.map {
                                if (it.id == schedule.id) it.copy(isSelected = !it.isSelected)
                                else it
                            }
                        }
                    )
                }
            }
        }
    }

    // Add Schedule Popup
    AddSchedulePopup(
        showDialog = showAddDialog,
        onDismiss = { showAddDialog = false },
        onConfirm = { time ->
            val newId = (schedules.maxOfOrNull { it.id } ?: 0) + 1
            schedules = schedules + FeedingSchedule(newId, time, true)
        }
    )
}

@Composable
fun ScheduleItem(
    schedule: FeedingSchedule,
    onToggleActive: () -> Unit,
    onToggleSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .border(
                width = if (schedule.isSelected) 2.dp else 0.dp,
                color = if (schedule.isSelected) colorResource(R.color.cyan_primer) else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.grey_second)
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Checkbox(
                checked = schedule.isSelected,
                onCheckedChange = { onToggleSelect() },
                modifier = Modifier.size(20.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(R.color.cyan_primer),
                    uncheckedColor = colorResource(R.color.grey_primer)
                )
            )

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = schedule.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.font_primer)
                )

                Text(
                    text = if (schedule.isActive) "Aktif" else "Non-Aktif",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (schedule.isActive) colorResource(R.color.cyan_primer) else Color.Gray
                )
            }

            Switch(
                checked = schedule.isActive,
                onCheckedChange = { onToggleActive() },
                modifier = Modifier.size(width = 40.dp, height = 24.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.cyan_primer),
                    checkedTrackColor = colorResource(R.color.cyan_primer).copy(alpha = 0.5f),
                    uncheckedThumbColor = colorResource(R.color.grey_primer),
                    uncheckedTrackColor = colorResource(R.color.white_outline_primer)
                )
            )
        }
    }
}

@Composable
fun AddSchedulePopup(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (LocalTime) -> Unit
) {
    var hour by remember { mutableStateOf("08") }
    var minute by remember { mutableStateOf("00") }
    var isValidTime by remember { mutableStateOf(true) }

    fun validateTime(h: String, m: String): Boolean {
        return try {
            val hourInt = h.toIntOrNull() ?: return false
            val minuteInt = m.toIntOrNull() ?: return false
            hourInt in 0..23 && minuteInt in 0..59
        } catch (e: Exception) {
            false
        }
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
                        painter = painterResource(id = R.drawable.ic_tab_schedule),
                        contentDescription = "Schedule Icon",
                        tint = colorResource(R.color.cyan_primer),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        "Tambah Jadwal Baru",
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
                        "Pilih waktu untuk jadwal pakan:",
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = hour,
                                    onValueChange = {
                                        if (it.length <= 2) {
                                            hour = it.filter { char -> char.isDigit() }
                                            isValidTime = validateTime(hour, minute)
                                        }
                                    },
                                    label = { Text("Jam", style = MaterialTheme.typography.labelMedium) },
                                    modifier = Modifier.width(90.dp),
                                    isError = !isValidTime,
                                    textStyle = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colorResource(R.color.cyan_primer),
                                        focusedLabelColor = colorResource(R.color.cyan_primer),
                                        errorBorderColor = colorResource(R.color.red_primer),
                                        errorLabelColor = colorResource(R.color.red_primer),
                                        unfocusedBorderColor = colorResource(R.color.white_outline_primer),
                                        unfocusedLabelColor = colorResource(R.color.grey_primer)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Text(
                                    ":",
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(R.color.cyan_primer)
                                )

                                OutlinedTextField(
                                    value = minute,
                                    onValueChange = {
                                        if (it.length <= 2) {
                                            minute = it.filter { char -> char.isDigit() }
                                            isValidTime = validateTime(hour, minute)
                                        }
                                    },
                                    label = { Text("Menit", style = MaterialTheme.typography.labelMedium) },
                                    modifier = Modifier.width(90.dp),
                                    isError = !isValidTime,
                                    textStyle = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colorResource(R.color.cyan_primer),
                                        focusedLabelColor = colorResource(R.color.cyan_primer),
                                        errorBorderColor = colorResource(R.color.red_primer),
                                        errorLabelColor = colorResource(R.color.red_primer),
                                        unfocusedBorderColor = colorResource(R.color.white_outline_primer),
                                        unfocusedLabelColor = colorResource(R.color.grey_primer)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            if (!isValidTime) {
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
                                            text = "Format waktu tidak valid\n(Jam: 00-23, Menit: 00-59)",
                                            color = colorResource(R.color.red_primer),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            } else {
                                // Preview time display
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = colorResource(R.color.cyan_primer).copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Preview: ${hour.padStart(2, '0')}:${minute.padStart(2, '0')}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(R.color.cyan_primer)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isValidTime && hour.isNotBlank() && minute.isNotBlank()) {
                            val time = LocalTime.of(hour.toInt(), minute.toInt())
                            onConfirm(time)
                            onDismiss()
                        }
                    },
                    enabled = isValidTime && hour.isNotBlank() && minute.isNotBlank(),
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
                        painter = painterResource(id = R.drawable.ic_tab_schedule),
                        contentDescription = "Add",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Tambah",
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
fun ScheduleScreenPreview() {
    ScheduleScreen()
}
