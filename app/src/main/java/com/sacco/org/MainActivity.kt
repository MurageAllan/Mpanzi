package com.sacco.org

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sacco.org.presentation.navigation.AppNavHost
import com.sacco.org.ui.theme.SaccoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            SaccoTheme {
                AppNavHost()
            }
        }
    }
}

