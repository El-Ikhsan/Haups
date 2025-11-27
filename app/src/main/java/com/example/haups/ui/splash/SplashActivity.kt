package com.example.haups.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.haups.MainActivity
import com.example.haups.ui.theme.HaupsTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HaupsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)) {
                        SplashScreen(
                            onNavigateToHome = {
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}
