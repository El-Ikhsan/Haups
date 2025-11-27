app/src/main/java/com/example/haups/
├── MainActivity.kt
├── PakanApplication.kt        // @HiltAndroidApp

├── core/
│   └── theme/
│       ├── Color.kt
│       ├── Typography.kt
│       └── Theme.kt

├── navigation/
│   ├── AppNav.kt              // NavHost utama
│   └── NavigationRoute.kt       // Model item bottom nav

├── components/
│   ├── PrimaryButton.kt
│   ├── InfoCard.kt
│   ├── StatusChip.kt
│   └── IconBox.kt

├── data/
│   ├── datastore/
│   │   └── FeederPreferences.kt   // DataStore
│   │
│   ├── database/
│   │   ├── AppDatabase.kt         // @Database Room
│   │   └── LogEntity.kt           // @Entity untuk log
│   │
│   └── repository/
│       ├── FeederRepository.kt    // Menggabungkan API + DB
│       └── LogRepository.kt

├── network/
│   └── Esp32Api.kt                // Retrofit interface

├── ui/
│   ├── splash/
│   │   └── SplashScreen.kt
│   │
│   ├── home/
│   │   └── HomeScreen.kt
│   │
│   ├── jadwal/
│   │   └── JadwalScreen.kt
│   │
│   ├── log/
│   │   └── LogScreen.kt
│   │
│   ├── pengaturan/
│   │   └── PengaturanScreen.kt
│   │
│   └── viewmodel/
│       ├── HomeViewModel.kt
│       ├── JadwalViewModel.kt
│       ├── LogViewModel.kt
│       └── PengaturanViewModel.kt

└── di/
├── DatabaseModule.kt          // Room
├── NetworkModule.kt           // Retrofit
└── RepositoryModule.kt        // Bind repository
