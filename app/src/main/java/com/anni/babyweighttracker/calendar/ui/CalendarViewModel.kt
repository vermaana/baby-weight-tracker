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
    private val _monthStatus = MutableStateFlow<ResultState<List<CalendarDay>>>(ResultState.Waiting)
    internal val monthStatus = _monthStatus.asStateFlow()

    private val _saveStatus = MutableStateFlow<ResultState<Boolean>>(ResultState.Waiting)
    internal val saveStatus = _saveStatus.asStateFlow()

    private var selectedMonth = LocalDate.now().month
    private var selectedYear = LocalDate.now().year

    internal fun loadDataForSelectedMonth() {
        viewModelScope.launch(ioDispatcher) {
            val days = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
            val todayDayOfWeek = LocalDate.of(selectedYear, selectedMonth, 1).dayOfWeek
            val totalDays = YearMonth.of(selectedYear, selectedMonth).lengthOfMonth()
            val calendarDays = arrayListOf<CalendarDay>()

            _monthStatus.value = ResultState.Loading

            //Get weight record for selected month
            val weightRecord = calendarRepository.getWeightRecordOfSpecificMonth(month = selectedMonth.value, year = selectedYear)

            //Add each date
            var pos = days.indexOf(todayDayOfWeek)
            for (date in 1..totalDays) {
                val weightInGrams = weightRecord?.firstOrNull { it.date == date }?.weightInGrams
                calendarDays.add(CalendarDay(date = date, month = selectedMonth.value, year = selectedYear, day = days[pos], weightInGrams = weightInGrams))
                pos++
                if (pos > days.size - 1) pos = 0
            }

            //Add dates in beginning that need to be skipped
            for (j in days.indexOf(calendarDays.first().day) - 1 downTo 0) {
                calendarDays.add(
                    index = 0,
                    element = CalendarDay(date = -1, month = selectedMonth.value, year = selectedYear, day = days[j], weightInGrams = null)
                )
            }

            _monthStatus.value = ResultState.Success(calendarDays)
        }
    }

    internal fun saveDataForSelectedDate(selectedCalendarDay: CalendarDay, weightInGrams: Int) {
        viewModelScope.launch(ioDispatcher) {
            _saveStatus.value = ResultState.Waiting
            val saveResult = calendarRepository.insertWeightRecordOfSpecificDate(calendarDay = selectedCalendarDay, weightInGrams = weightInGrams)
            _saveStatus.value = ResultState.Success(saveResult)
        }
    }

    internal fun nextMonth() {
        selectedMonth = selectedMonth.plus(1)
        if (selectedMonth == Month.JANUARY)
            selectedYear = selectedYear.plus(1)
    }

    internal fun previousMonth() {
        selectedMonth = selectedMonth.minus(1)
        if (selectedMonth == Month.DECEMBER)
            selectedYear = selectedYear.minus(1)
    }

    internal fun resetMonthStatus() {
        _monthStatus.value = ResultState.Waiting
    }

    internal fun resetSaveStatus() {
        _saveStatus.value = ResultState.Waiting
    }

    internal fun getCurrentMonthAsString(): String = selectedMonth.name

    internal fun getCurrentYearAsString(): String = selectedYear.toString()
}
