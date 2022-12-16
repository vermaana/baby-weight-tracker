package com.anni.babyweighttracker.mapper

import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.common.util.ModelMapper
import com.anni.babyweighttracker.data.weight.BabyWeightRecord

class CalendarDayToBabyWeightRecordMapper : ModelMapper<CalendarDay, BabyWeightRecord> {
    override fun sourceToDestination(sourceModel: CalendarDay): BabyWeightRecord = sourceToDestination(sourceModel = sourceModel, id = null)

    fun sourceToDestination(sourceModel: CalendarDay, id: Int?): BabyWeightRecord {
        return BabyWeightRecord(
            id = id,
            weightInGrams = sourceModel.weightInGrams!!,
            entryDate = sourceModel.date,
            entryMonth = sourceModel.month,
            entryYear = sourceModel.year
        )
    }
}
