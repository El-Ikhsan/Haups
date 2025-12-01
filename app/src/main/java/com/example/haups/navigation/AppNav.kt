package com.example.haups.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.haups.R
import com.example.haups.ui.home.HomeScreen

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val items = listOf(
        NavigationRoute.Home,
        NavigationRoute.Schedule,
        NavigationRoute.Logs,
        NavigationRoute.Settings
    )

    val selectedColor = colorResource(id = R.color.cyan_primer)
    val unselectedColor = colorResource(id = R.color.grey_primer)

    Scaffold(
        bottomBar = {
            // PERBAIKAN 1: Warna dipindah ke Surface agar Shadow muncul
            androidx.compose.material3.Surface(
                border = BorderStroke(
                    width = 4.dp,              // Ketebalan garis
                    colorResource(id = R.color.white_outline_primer)// Warna garis (Ganti sesuai selera, misal Color.Gray)
                ),
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, bottom = 20.dp)
                    .height(70.dp)
                    .width(350.dp),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 10.dp, // Shadow akan terlihat jelas sekarang
                color = colorResource(id = R.color.dark_primer) // Warna background di sini
            ) {
                NavigationBar(
                    containerColor = Color.Transparent, // Navigasi jadi transparan
                    tonalElevation = 0.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    items.forEach { screen ->
                        // ... (Logika Icon dan LabelRes sama seperti punya Anda) ...
                        val iconRes = when (screen) {
                            NavigationRoute.Home -> R.drawable.ic_tab_home
                            NavigationRoute.Schedule -> R.drawable.ic_tab_schedule
                            NavigationRoute.Logs -> R.drawable.ic_tab_logs
                            NavigationRoute.Settings -> R.drawable.ic_tab_settings
                            else -> R.mipmap.ic_launcher // Default icon jika error
                        }

                        val labelRes = when (screen) {
                            NavigationRoute.Home -> R.string.tab_home
                            NavigationRoute.Schedule -> R.string.tab_schedule
                            NavigationRoute.Logs -> R.string.tab_logs
                            NavigationRoute.Settings -> R.string.tab_settings
                            else -> R.string.app_name
                        }

                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up ke start destination agar backstack tidak menumpuk
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Mencegah membuka layar yang sama berulang kali saat diklik
                                    launchSingleTop = true
                                    // Restore state saat kembali (misal scroll position tidak hilang)
                                    restoreState = true
                                }
                            },
                            alwaysShowLabel = false,
                            modifier = Modifier.offset(y = 12.dp),
                            icon = {
                                // BUNGKUS ICON DENGAN BOX UNTUK CUSTOM INDICATOR
                                androidx.compose.foundation.layout.Box(
                                    contentAlignment = androidx.compose.ui.Alignment.Center,
                                    modifier = Modifier
                                        // --- ATUR UKURAN LINGKARAN DI SINI ---
                                        .size(50.dp) // <-- Ganti angka ini (misal 40.dp lebih kecil, 60.dp lebih besar)
                                        // -------------------------------------

                                        // Bentuk Lingkaran (Bisa ganti RoundedCornerShape(10.dp) kalau mau kotak tumpul)
                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))

                                        // Logika Warna Background (Hanya muncul jika selected)
                                        .background(
                                            if (currentRoute == screen.route) selectedColor else Color.Transparent
                                        )
                                ) {
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        // Icon tint: Putih jika dipilih (karena background berwarna), Abu jika tidak
                                        tint = if (currentRoute == screen.route) colorResource(R.color.dark_primer) else unselectedColor,
                                        modifier = Modifier.size(24.dp) // Ukuran Icon di dalam lingkaran
                                    )
                                }
                            },

                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = selectedColor,
                                unselectedIconColor = unselectedColor,
                                indicatorColor = Color.Transparent // Opsional: Menghilangkan lingkaran sorotan saat diklik
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ... (Isi NavHost sama seperti punya Anda) ...
            composable(NavigationRoute.Home.route) {
                // HomeScreen signature no longer accepts onOpenSettings; call without the removed parameter
                HomeScreen()
            }
            composable(NavigationRoute.Schedule.route) {
                com.example.haups.ui.schedule.ScheduleScreen()
            }
            composable(NavigationRoute.Logs.route) {
                com.example.haups.ui.logs.LogsScreen()
            }
            composable(NavigationRoute.Settings.route) {
                com.example.haups.ui.settings.SettingsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}