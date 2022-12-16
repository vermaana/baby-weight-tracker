package com.anni.babyweighttracker.calendar.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.anni.babyweighttracker.R
import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.ui.theme.DARK_PINK_HEADING
import com.anni.babyweighttracker.common.ui.theme.GREEN
import com.anni.babyweighttracker.common.ui.theme.LIGHT_PINK_BACKGROUND
import com.anni.babyweighttracker.common.ui.theme.LIGHT_PINK_HIGHLIGHT
import com.anni.babyweighttracker.common.util.ResultState
import java.time.DayOfWeek

internal const val CALENDAR_SCREEN_ROUTE = "CALENDAR_SCREEN_ROUTE"

private const val NOT_AVAILABLE = "NA"

internal fun NavGraphBuilder.calendarScreen(calendarViewModel: CalendarViewModel, enterWeight: (CalendarDay) -> Unit) {
    composable(route = CALENDAR_SCREEN_ROUTE) {
        CalendarScreen(calendarViewModel = calendarViewModel, enterWeight = enterWeight)
    }
}

@Composable
private fun CalendarScreen(calendarViewModel: CalendarViewModel, enterWeight: (CalendarDay) -> Unit) {
    val monthStatus = calendarViewModel.monthStatus.collectAsState().value
    val todayDate = calendarViewModel.getTodayDate()
    val todayMonth = calendarViewModel.getTodayMonth()
    val todayYear = calendarViewModel.getTodayYear()
    val daysOfWeek = mapOf(
        DayOfWeek.SUNDAY to R.string.sunday,
        DayOfWeek.MONDAY to R.string.monday,
        DayOfWeek.TUESDAY to R.string.tuesday,
        DayOfWeek.WEDNESDAY to R.string.wednesday,
        DayOfWeek.THURSDAY to R.string.thursday,
        DayOfWeek.FRIDAY to R.string.friday,
        DayOfWeek.SATURDAY to R.string.saturday,
    )

    Surface(color = LIGHT_PINK_BACKGROUND) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Heading()
            when (monthStatus) {
                is ResultState.Waiting -> calendarViewModel.loadDataForSelectedMonth()
                is ResultState.Loading -> CircularProgressIndicator()
                is ResultState.Success -> {
                    Column {
                        CalendarHeading(
                            monthYear = "${calendarViewModel.getCurrentMonthAsString()} ${calendarViewModel.getCurrentYearAsString()}",
                            nextButtonClicked = {
                                calendarViewModel.resetMonthStatus()
                                calendarViewModel.nextMonth()
                            },
                            previousButtonClicked = {
                                calendarViewModel.resetMonthStatus()
                                calendarViewModel.previousMonth()
                            }
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            daysOfWeek.forEach { entry ->
                                CalendarDates(
                                    day = stringResource(id = entry.value),
                                    affectedDay = monthStatus.result.filter { it.day == entry.key },
                                    todayDate = todayDate,
                                    todayMonth = todayMonth,
                                    todayYear = todayYear
                                ) {
                                    calendarViewModel.resetSaveStatus()
                                    enterWeight(it)
                                }
                            }
                        }
                    }
                }
                is ResultState.Error -> Toast.makeText(LocalContext.current, stringResource(id = R.string.error_occurred_try_again), Toast.LENGTH_SHORT).show()
            }
            Footer()
        }
    }
}

@Composable
private fun Heading() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        text = stringResource(R.string.app_name),
        textAlign = TextAlign.Center,
        fontSize = 32.sp
    )
}

@Composable
private fun CalendarHeading(monthYear: String, nextButtonClicked: () -> Unit, previousButtonClicked: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = monthYear, textAlign = TextAlign.Start, fontSize = 24.sp, color = DARK_PINK_HEADING)
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
private fun CalendarDates(day: String, affectedDay: List<CalendarDay>, todayDate: Int, todayMonth: Int, todayYear: Int, dateClicked: (CalendarDay) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontWeight = FontWeight.Bold)
        affectedDay.forEach {
            val weightInGrams = if (it.weightInGrams == null) "($NOT_AVAILABLE)" else "(${it.weightInGrams}g)"
            val toDisplay = if (it.date == -1) Pair("", "") else Pair("${it.date}", weightInGrams)
            Spacer(modifier = Modifier.size(10.dp))
            Column(
                modifier = Modifier
                    .clickable {
                        if (it.date != -1)
                            dateClicked(it)
                    }
                    .drawBehind {
                        if (it.date == todayDate && it.month == todayMonth && it.year == todayYear)
                            drawCircle(color = LIGHT_PINK_HIGHLIGHT, radius = (this.size.maxDimension - (this.size.maxDimension * 0.3)).toFloat())
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = toDisplay.first)
                if (toDisplay.second == "($NOT_AVAILABLE)")
                    Text(text = toDisplay.second, fontSize = 10.sp)
                else
                    Text(text = toDisplay.second, fontSize = 10.sp, color = GREEN)
            }
        }
    }
}

@Composable
private fun Footer() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 20.dp),
        text = stringResource(id = R.string.made_with_love_for_aayu),
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp,
        color = DARK_PINK_HEADING
    )
}
