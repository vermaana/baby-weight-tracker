package com.anni.babyweighttracker.mapper

import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.util.ModelMapper
import com.anni.babyweighttracker.data.weight.BabyWeightRecord
import java.time.LocalDate

class BabyWeightRecordToCalendarDayMapper : ModelMapper<BabyWeightRecord, CalendarDay> {
    override fun sourceToDestination(sourceModel: BabyWeightRecord): CalendarDay {
        val selectedDate = LocalDate.of(sourceModel.entryYear, sourceModel.entryMonth, sourceModel.entryDate)
        return CalendarDay(
            date = sourceModel.entryDate,
            month = sourceModel.entryMonth,
            year = sourceModel.entryYear,
            day = selectedDate.dayOfWeek,
            weightInGrams = sourceModel.weightInGrams
        )
    }
}
