package com.anni.babyweighttracker.calendar.ui

import androidx.lifecycle.ViewModel
import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.util.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

internal class CalendarViewModel : ViewModel() {
    private val _calendarDaysStatus = MutableStateFlow<ResultState<List<CalendarDay>>>(ResultState.Waiting)
    internal val calendarDaysStatus = _calendarDaysStatus.asStateFlow()

    private var todayMonth = LocalDate.now().month
    private var todayYear = LocalDate.now().year

    internal fun loadCalendarDaysForSelectedMonth() {
        val days = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
        val todayDayOfWeek = LocalDate.of(todayYear, todayMonth, 1).dayOfWeek
        val totalDays = YearMonth.of(todayYear, todayMonth).lengthOfMonth()
        val calendarDays = arrayListOf<CalendarDay>()

        _calendarDaysStatus.value = ResultState.Loading

        //Add each date
        var pos = days.indexOf(todayDayOfWeek)
        for (date in 1..totalDays) {
            calendarDays.add(CalendarDay(date = date, month = todayMonth.value, year = todayYear, day = days[pos]))
            pos++
            if (pos > days.size - 1) pos = 0
        }

        //Add dates in beginning that need to be skipped
        for (j in days.indexOf(calendarDays.first().day) - 1 downTo 0) {
            calendarDays.add(
                index = 0,
                element = CalendarDay(date = -1, month = todayMonth.value, year = todayYear, day = days[j])
            )
        }

        _calendarDaysStatus.value = ResultState.Success(calendarDays)
    }

    internal fun nextMonth() {
        todayMonth = todayMonth.plus(1)
        if (todayMonth == Month.JANUARY)
            todayYear = todayYear.plus(1)
        _calendarDaysStatus.value = ResultState.Waiting
    }

    internal fun previousMonth() {
        todayMonth = todayMonth.minus(1)
        if (todayMonth == Month.DECEMBER)
            todayYear = todayYear.minus(1)
        _calendarDaysStatus.value = ResultState.Waiting
    }

    internal fun getCurrentMonthAsString(): String = todayMonth.name

    internal fun getCurrentYearAsString(): String = todayYear.toString()
}
