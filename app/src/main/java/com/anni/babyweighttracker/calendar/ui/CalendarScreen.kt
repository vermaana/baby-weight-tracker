package com.anni.babyweighttracker.calendar.ui

import androidx.compose.foundation.clickable
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anni.babyweighttracker.R
import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.util.ResultState
import java.time.DayOfWeek

internal const val CALENDAR_SCREEN_ROUTE = "CALENDAR_SCREEN_ROUTE"

internal fun NavGraphBuilder.calendarScreen(calendarViewModel: CalendarViewModel, enterWeight: (CalendarDay) -> Unit) {
    composable(route = CALENDAR_SCREEN_ROUTE) {
        CalendarScreen(calendarViewModel = calendarViewModel, enterWeight = enterWeight)
    }
}

@Composable
private fun CalendarScreen(calendarViewModel: CalendarViewModel, enterWeight: (CalendarDay) -> Unit) {
    val monthStatus = calendarViewModel.monthStatus.collectAsState().value
    val daysOfWeek = mapOf(
        DayOfWeek.SUNDAY to R.string.sunday,
        DayOfWeek.MONDAY to R.string.monday,
        DayOfWeek.TUESDAY to R.string.tuesday,
        DayOfWeek.WEDNESDAY to R.string.wednesday,
        DayOfWeek.THURSDAY to R.string.thursday,
        DayOfWeek.FRIDAY to R.string.friday,
        DayOfWeek.SATURDAY to R.string.saturday,
    )

    Surface {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Heading()
            Spacer(modifier = Modifier.size(60.dp))

            when (monthStatus) {
                is ResultState.Waiting -> calendarViewModel.loadDataForSelectedMonth()
                is ResultState.Loading -> CircularProgressIndicator()
                is ResultState.Success -> {
                    CalendarHeading(
                        monthYear = "${calendarViewModel.getCurrentMonthAsString()} ${calendarViewModel.getCurrentYearAsString()}",
                        nextButtonClicked = { calendarViewModel.nextMonth() },
                        previousButtonClicked = { calendarViewModel.previousMonth() }
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        daysOfWeek.forEach { entry ->
                            CalendarDates(day = stringResource(id = entry.value), affectedDay = monthStatus.result.filter { it.day == entry.key }) {
                                enterWeight(it)
                            }
                        }
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
private fun CalendarDates(day: String, affectedDay: List<CalendarDay>, dateClicked: (CalendarDay) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontWeight = FontWeight.Bold)
        affectedDay.forEach {
            val weightInGrams = if (it.weightInGrams == null) "(No entry)" else it.weightInGrams.toString()
            val toDisplay = if (it.date == -1) Pair("", "") else Pair("${it.date}", weightInGrams)
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier.clickable {
                    if (it.date != -1)
                        dateClicked(it)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = toDisplay.first)
                Text(text = toDisplay.second, fontSize = 10.sp)
            }
        }
    }
}
