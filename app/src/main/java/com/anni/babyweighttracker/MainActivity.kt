package com.anni.babyweighttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.anni.babyweighttracker.common.ui.theme.BabyWeightTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BabyWeightTrackerTheme {
                AppNavigation()
            }
        }
    }
}
