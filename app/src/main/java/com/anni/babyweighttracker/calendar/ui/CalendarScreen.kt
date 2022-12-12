package com.anni.babyweighttracker.calendar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anni.babyweighttracker.R
import com.anni.babyweighttracker.calendar.model.CalendarDay
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

internal const val CALENDAR_SCREEN_ROUTE = "CALENDAR_SCREEN_ROUTE"

internal fun NavGraphBuilder.calendarScreenRoute() {
    composable(route = CALENDAR_SCREEN_ROUTE) {
        CalendarScreen()
    }
}

@Composable
private fun CalendarScreen() {
    val month = LocalDate.now().month
    val year = LocalDate.now().year
    val dayOfWeek = LocalDate.of(year, month, 1).dayOfWeek
    val totalDays = YearMonth.of(year, month).lengthOfMonth()
    val testwa = getTest(startingDay = dayOfWeek, totalDays = totalDays)
    println("Anni: $testwa")

    Surface {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Text(modifier = Modifier.fillMaxWidth(), text = stringResource(R.string.app_name), textAlign = TextAlign.Center, fontSize = 32.sp)
            Spacer(modifier = Modifier.size(60.dp))
            CalendarHeading(monthYear = "September 2022")
            Spacer(modifier = Modifier.size(20.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                val sundays = testwa.filter { it.day == DayOfWeek.SUNDAY }
                val mondays = testwa.filter { it.day == DayOfWeek.MONDAY }
                val tuesdays = testwa.filter { it.day == DayOfWeek.TUESDAY }
                val wednesdays = testwa.filter { it.day == DayOfWeek.WEDNESDAY }
                val thursdays = testwa.filter { it.day == DayOfWeek.THURSDAY }
                val fridays = testwa.filter { it.day == DayOfWeek.FRIDAY }
                val saturdays = testwa.filter { it.day == DayOfWeek.SATURDAY }

                CalendarDates(day = "Sun", sundays)
                CalendarDates(day = "Mon", mondays)
                CalendarDates(day = "Tue", tuesdays)
                CalendarDates(day = "Wed", wednesdays)
                CalendarDates(day = "Thu", thursdays)
                CalendarDates(day = "Fri", fridays)
                CalendarDates(day = "Sat", saturdays)
            }
        }
    }
}

@Composable
private fun CalendarHeading(monthYear: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = monthYear, textAlign = TextAlign.Start, fontSize = 24.sp)
        Row {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.ArrowBack, "")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.ArrowForward, "")
            }
        }
    }
}

@Composable
private fun CalendarDates(day: String, affectedDay: List<CalendarDay>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day)
        affectedDay.forEach {
            val toDisplay = if (it.date == -1) Pair("", "") else Pair("${it.date}", "(9999g)")
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = toDisplay.first)
            Text(text = toDisplay.second, fontSize = 10.sp)
        }
    }
}

private fun getTest(startingDay: DayOfWeek, totalDays: Int): List<CalendarDay> {
    val days = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
    val calendarDays = arrayListOf<CalendarDay>()

    //Add each date
    var pos = days.indexOf(startingDay)
    for (date in 1..totalDays) {
        calendarDays.add(CalendarDay(day = days[pos], date = date))
        pos++
        if (pos > days.size - 1) pos = 0
    }

    //Add dates in beginning that need to be skipped
    for (j in days.indexOf(calendarDays.first().day) - 1 downTo 0) calendarDays.add(index = 0, element = CalendarDay(day = days[j], date = -1))

    return calendarDays
}
