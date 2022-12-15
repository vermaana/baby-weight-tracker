package com.anni.babyweighttracker.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anni.babyweighttracker.calendar.model.CalendarDay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal const val WEIGHT_ENTRY_SCREEN_ROUTE = "WEIGHT_ENTRY_SCREEN_ROUTE"

internal fun NavGraphBuilder.weightEntryScreen(calendarViewModel: CalendarViewModel) {
    composable(route = "$WEIGHT_ENTRY_SCREEN_ROUTE/{selectedCalendarDay}") {
        val selectedCalendarDayString = it.arguments?.getString("selectedCalendarDay")
        val selectedCalendarDay = Json.decodeFromString<CalendarDay>(selectedCalendarDayString!!)
        WeightEntryScreen(calendarViewModel = calendarViewModel, selectedCalendarDay = selectedCalendarDay)
    }
}

@Composable
private fun WeightEntryScreen(calendarViewModel: CalendarViewModel, selectedCalendarDay: CalendarDay) {
    val enteredWeight = rememberSaveable { mutableStateOf("") }

    Surface {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                text = "Enter weight for ${selectedCalendarDay.day}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                text = "(${selectedCalendarDay.date}-${selectedCalendarDay.month}-${selectedCalendarDay.year})",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.size(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    label = { Text(text = "Enter weight here") },
                    value = enteredWeight.value,
                    onValueChange = { enteredWeight.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    maxLines = 1
                )
                Text(modifier = Modifier.padding(start = 10.dp), text = "grams")
            }
            Spacer(modifier = Modifier.size(40.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Submit")
            }
        }
    }
}
