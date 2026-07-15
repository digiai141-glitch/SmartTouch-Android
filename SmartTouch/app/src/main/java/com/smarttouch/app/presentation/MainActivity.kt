package com.smarttouch.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.smarttouch.app.presentation.navigation.SmartTouchNavGraph
import com.smarttouch.app.presentation.ui.theme.SmartTouchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartTouchTheme {
                val navController = rememberNavController()
                SmartTouchNavGraph(navController = navController)
            }
        }
    }
}
