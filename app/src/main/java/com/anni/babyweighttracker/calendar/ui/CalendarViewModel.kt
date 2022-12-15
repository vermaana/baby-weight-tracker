package com.anni.babyweighttracker.calendar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.calendar.repository.CalendarRepository
import com.anni.babyweighttracker.common.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
internal class CalendarViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository,
    @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _calendarDaysStatus = MutableStateFlow<ResultState<List<CalendarDay>>>(ResultState.Waiting)
    internal val calendarDaysStatus = _calendarDaysStatus.asStateFlow()

    private var todayMonth = LocalDate.now().month
    private var todayYear = LocalDate.now().year

    internal fun loadCalendarDaysForSelectedMonth() {
        viewModelScope.launch(ioDispatcher) {
            val days = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
            val todayDayOfWeek = LocalDate.of(todayYear, todayMonth, 1).dayOfWeek
            val totalDays = YearMonth.of(todayYear, todayMonth).lengthOfMonth()
            val calendarDays = arrayListOf<CalendarDay>()

            _calendarDaysStatus.value = ResultState.Loading

            //Get weight record for selected month
            val weightRecord = calendarRepository.getWeightRecordOfSpecificMonth(month = todayMonth.value, year = todayYear)

            //Add each date
            var pos = days.indexOf(todayDayOfWeek)
            for (date in 1..totalDays) {
                val weightInGrams = weightRecord?.firstOrNull { it.entryDate == date }?.weightInGrams
                calendarDays.add(CalendarDay(date = date, month = todayMonth.value, year = todayYear, day = days[pos], weightInGrams = weightInGrams))
                pos++
                if (pos > days.size - 1) pos = 0
            }

            //Add dates in beginning that need to be skipped
            for (j in days.indexOf(calendarDays.first().day) - 1 downTo 0) {
                calendarDays.add(
                    index = 0,
                    element = CalendarDay(date = -1, month = todayMonth.value, year = todayYear, day = days[j], weightInGrams = null)
                )
            }

            _calendarDaysStatus.value = ResultState.Success(calendarDays)
        }
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
