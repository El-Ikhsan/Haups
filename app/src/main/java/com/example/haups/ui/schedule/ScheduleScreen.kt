package com.example.haups.ui.schedule

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.haups.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.tooling.preview.Preview

data class FeedingSchedule(
    val id: Int,
    val time: LocalTime,
    val isActive: Boolean,
    val isSelected: Boolean = false
)

@Composable
fun ScheduleScreen() {
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var schedules by remember {
        mutableStateOf(listOf(
            FeedingSchedule(1, LocalTime.of(8, 0), true),
            FeedingSchedule(2, LocalTime.of(12, 30), false),
            FeedingSchedule(3, LocalTime.of(18, 0), true)
        ))
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header
            item {
                Text(
                    text = "Jadwal Pakan",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // 2. Calendar box with month and year
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 3. Days of week with dates
            item {
                val startOfWeek = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(7) { index ->
                        val date = startOfWeek.plusDays(index.toLong())
                        val isToday = date == LocalDate.now()

                        Card(
                            modifier = Modifier
                                .size(60.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isToday) MaterialTheme.colorScheme.primary
                                               else MaterialTheme.colorScheme.surface
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
                                    color = if (isToday) MaterialTheme.colorScheme.onPrimary
                                           else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isToday) MaterialTheme.colorScheme.onPrimary
                                           else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // 4. Time display with large font
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 5. Add and Delete buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val newId = (schedules.maxOfOrNull { it.id } ?: 0) + 1
                            schedules = schedules + FeedingSchedule(newId, selectedTime, true)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Tambah Jadwal")
                    }

                    OutlinedButton(
                        onClick = {
                            schedules = schedules.filter { !it.isSelected }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("🗑️ Hapus")
                    }
                }
            }

            // 6. Schedule list
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

@Composable
fun ScheduleItem(
    schedule: FeedingSchedule,
    onToggleActive: () -> Unit,
    onToggleSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (schedule.isSelected) 2.dp else 0.dp,
                color = if (schedule.isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox for selection
            Checkbox(
                checked = schedule.isSelected,
                onCheckedChange = { onToggleSelect() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Time with large font
            Text(
                text = schedule.time.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Status
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (schedule.isActive) "Jadwal Aktif" else "Jadwal Tidak Aktif",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (schedule.isActive) Color.Green else Color.Gray
                )
            }

            // Toggle switch
            Switch(
                checked = schedule.isActive,
                onCheckedChange = { onToggleActive() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen()
}
