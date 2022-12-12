package com.anni.babyweighttracker.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anni.babyweighttracker.R
import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.util.ResultState
import java.time.DayOfWeek

internal const val CALENDAR_SCREEN_ROUTE = "CALENDAR_SCREEN_ROUTE"

internal fun NavGraphBuilder.calendarScreenRoute() {
    composable(route = CALENDAR_SCREEN_ROUTE) {
        CalendarScreen()
    }
}

@Composable
private fun CalendarScreen(calendarViewModel: CalendarViewModel = viewModel()) {
    val calendarDaysStatus = calendarViewModel.calendarDaysStatus.collectAsState().value

    Surface {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Heading()
            Spacer(modifier = Modifier.size(60.dp))

            when (calendarDaysStatus) {
                is ResultState.Waiting -> calendarViewModel.loadCalendarDaysForSelectedMonth()
                is ResultState.Loading -> CircularProgressIndicator()
                is ResultState.Success -> {
                    CalendarHeading(
                        monthYear = "${calendarViewModel.getCurrentMonthAsString()} ${calendarViewModel.getCurrentYearAsString()}",
                        nextButtonClicked = { calendarViewModel.nextMonth() },
                        previousButtonClicked = { calendarViewModel.previousMonth() }
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        CalendarDates(day = stringResource(id = R.string.sunday), calendarDaysStatus.result.filter { it.day == DayOfWeek.SUNDAY })
                        CalendarDates(day = stringResource(id = R.string.monday), calendarDaysStatus.result.filter { it.day == DayOfWeek.MONDAY })
                        CalendarDates(day = stringResource(id = R.string.tuesday), calendarDaysStatus.result.filter { it.day == DayOfWeek.TUESDAY })
                        CalendarDates(day = stringResource(id = R.string.wednesday), calendarDaysStatus.result.filter { it.day == DayOfWeek.WEDNESDAY })
                        CalendarDates(day = stringResource(id = R.string.thursday), calendarDaysStatus.result.filter { it.day == DayOfWeek.THURSDAY })
                        CalendarDates(day = stringResource(id = R.string.friday), calendarDaysStatus.result.filter { it.day == DayOfWeek.FRIDAY })
                        CalendarDates(day = stringResource(id = R.string.saturday), calendarDaysStatus.result.filter { it.day == DayOfWeek.SATURDAY })
                    }
                }
                is ResultState.Error -> {
                    //TODO implement
                }
            }
        }
    }
}

@Composable
private fun Heading() {
    Text(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.app_name), textAlign = TextAlign.Center, fontSize = 32.sp)
}

@Composable
private fun CalendarHeading(monthYear: String, nextButtonClicked: () -> Unit, previousButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = monthYear, textAlign = TextAlign.Start, fontSize = 24.sp)
        Row {
            IconButton(onClick = previousButtonClicked) {
                Icon(imageVector = Icons.Filled.ArrowBack, "")
            }
            IconButton(onClick = nextButtonClicked) {
                Icon(imageVector = Icons.Filled.ArrowForward, "")
            }
        }
    }
}

@Composable
private fun CalendarDates(day: String, affectedDay: List<CalendarDay>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontWeight = FontWeight.Bold)
        affectedDay.forEach {
            val toDisplay = if (it.date == -1) Pair("", "") else Pair("${it.date}", "(9999g)")
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = toDisplay.first)
            Text(text = toDisplay.second, fontSize = 10.sp)
        }
    }
}
