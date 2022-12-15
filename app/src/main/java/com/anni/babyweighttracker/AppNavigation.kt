package com.anni.babyweighttracker

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.anni.babyweighttracker.calendar.ui.*
import com.anni.babyweighttracker.calendar.ui.CALENDAR_SCREEN_ROUTE
import com.anni.babyweighttracker.calendar.ui.WEIGHT_ENTRY_SCREEN_ROUTE
import com.anni.babyweighttracker.calendar.ui.calendarScreen
import com.anni.babyweighttracker.calendar.ui.weightEntryScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val calendarViewModel = viewModel<CalendarViewModel>()
    NavHost(navController = navController, startDestination = CALENDAR_SCREEN_ROUTE) {
        calendarScreen(calendarViewModel = calendarViewModel) { calendarDay ->
            val calendarDayString = Json.encodeToString(calendarDay)
            navController.navigate("$WEIGHT_ENTRY_SCREEN_ROUTE/$calendarDayString")
        }
        weightEntryScreen(calendarViewModel = calendarViewModel)
    }
}
