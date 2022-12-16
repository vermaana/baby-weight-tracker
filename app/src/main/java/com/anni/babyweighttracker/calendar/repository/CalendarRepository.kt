package com.anni.babyweighttracker.calendar.repository

import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.data.weight.BabyWeightDao
import com.anni.babyweighttracker.mapper.BabyWeightRecordToCalendarDayMapper
import com.anni.babyweighttracker.mapper.CalendarDayToBabyWeightRecordMapper
import javax.inject.Inject

class CalendarRepository @Inject constructor(
    private val babyWeightDao: BabyWeightDao,
    private val calendarDayToBabyWeightRecordMapper: CalendarDayToBabyWeightRecordMapper,
    private val babyWeightRecordToCalendarDayMapper: BabyWeightRecordToCalendarDayMapper
) {
    suspend fun getWeightRecordOfSpecificMonth(month: Int, year: Int): List<CalendarDay>? {
        val babyWeights = babyWeightDao.getWeightRecordsForAMonth(month = month, year = year)
        return if (babyWeights.isNotEmpty())
            babyWeightRecordToCalendarDayMapper.sourceToDestination(sourceModel = babyWeights)
        else null
    }

    suspend fun insertWeightRecordOfSpecificDate(calendarDay: CalendarDay, weightInGrams: Int): Boolean {
        val weightRecord = babyWeightDao.getWeightRecordsForADate(date = calendarDay.date, month = calendarDay.month, year = calendarDay.year)
        return if (weightRecord.isNotEmpty()) {
            val id = weightRecord.first().id
            val toSaveCalendarDay = calendarDay.copy(weightInGrams = weightInGrams)
            val babyWeight = calendarDayToBabyWeightRecordMapper.sourceToDestination(sourceModel = toSaveCalendarDay, id = id)
            val updateId = babyWeightDao.updateWeightRecordForADate(babyWeightRecord = babyWeight)
            updateId > 0
        }
        else {
            val toSaveCalendarDay = calendarDay.copy(weightInGrams = weightInGrams)
            val babyWeight = calendarDayToBabyWeightRecordMapper.sourceToDestination(sourceModel = toSaveCalendarDay)
            val insertId = babyWeightDao.insertWeightRecordForADate(babyWeightRecord = babyWeight)
            insertId > 0
        }
    }
}
