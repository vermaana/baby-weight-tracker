package com.anni.babyweighttracker.calendar.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anni.babyweighttracker.R
import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.util.ResultState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal const val WEIGHT_ENTRY_SCREEN_ROUTE = "WEIGHT_ENTRY_SCREEN_ROUTE"

internal fun NavGraphBuilder.weightEntryScreen(calendarViewModel: CalendarViewModel, upIconClicked: () -> Unit) {
    composable(route = "$WEIGHT_ENTRY_SCREEN_ROUTE/{selectedCalendarDay}") {
        val selectedCalendarDayString = it.arguments?.getString("selectedCalendarDay")
        val selectedCalendarDay = Json.decodeFromString<CalendarDay>(selectedCalendarDayString!!)
        WeightEntryScreen(calendarViewModel = calendarViewModel, selectedCalendarDay = selectedCalendarDay, upIconClicked = upIconClicked)
    }
}

@Composable
private fun WeightEntryScreen(calendarViewModel: CalendarViewModel, selectedCalendarDay: CalendarDay, upIconClicked: () -> Unit) {
    val saveStatus = calendarViewModel.saveStatus.collectAsState().value
    val enteredWeight = rememberSaveable { mutableStateOf("") }
    val showEntryError = rememberSaveable { mutableStateOf(Pair(false, "")) }

    Surface {
        IconButton(modifier = Modifier.padding(start = 20.dp, top = 40.dp), onClick = upIconClicked) {
            Icon(imageVector = Icons.Filled.ArrowBack, "")
        }
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                text = stringResource(id = R.string.enter_weight_for_value, selectedCalendarDay.day),
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
                Column {
                    TextField(
                        label = { Text(text = stringResource(id = R.string.enter_weight_here)) },
                        value = enteredWeight.value,
                        onValueChange = { enteredWeight.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        maxLines = 1,
                        isError = showEntryError.value.first,
                    )
                    if (showEntryError.value.first)
                        Text(text = showEntryError.value.second, color = Color.Red, fontSize = 12.sp)
                }
                Text(modifier = Modifier.padding(start = 10.dp), text = stringResource(id = R.string.grams))
            }
            Spacer(modifier = Modifier.size(40.dp))

            when (saveStatus) {
                is ResultState.Waiting -> {
                    Button(onClick = {
                        val weightToSave = enteredWeight.value.trim()
                        if (weightToSave.isEmpty() || weightToSave.toIntOrNull() == null)
                            showEntryError.value = Pair(true, "Please enter correct value here")
                        else {
                            showEntryError.value = Pair(false, "")
                            calendarViewModel.saveDataForSelectedDate(selectedCalendarDay = selectedCalendarDay, weightInGrams = weightToSave.toInt())
                        }
                    }) {
                        Text(text = stringResource(id = R.string.submit))
                    }
                }
                is ResultState.Loading -> CircularProgressIndicator()
                is ResultState.Success -> {
                    Toast.makeText(LocalContext.current, stringResource(id = R.string.weight_record_successfully_added), Toast.LENGTH_SHORT).show()
                    calendarViewModel.resetMonthStatus()
                    //TODO navigate back
                }
                is ResultState.Error -> {
                    Toast.makeText(LocalContext.current, stringResource(id = R.string.error_occurred_try_again), Toast.LENGTH_SHORT).show()
                    calendarViewModel.resetSaveStatus()
                }
            }
        }
    }
}
