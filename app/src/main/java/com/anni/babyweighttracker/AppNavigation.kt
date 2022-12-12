package com.anni.babyweighttracker

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.anni.babyweighttracker.calendar.ui.CALENDAR_SCREEN_ROUTE
import com.anni.babyweighttracker.calendar.ui.calendarScreenRoute

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =CALENDAR_SCREEN_ROUTE) {
        calendarScreenRoute()
    }
}
