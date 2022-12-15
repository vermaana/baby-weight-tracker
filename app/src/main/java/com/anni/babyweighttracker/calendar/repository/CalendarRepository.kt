package com.anni.babyweighttracker.calendar.repository

import com.anni.babyweighttracker.calendar.model.CalendarDay
import com.anni.babyweighttracker.data.weight.BabyWeightDao
import com.anni.babyweighttracker.mapper.BabyWeightToCalendarDayMapper
import javax.inject.Inject

class CalendarRepository @Inject constructor(
    private val babyWeightDao: BabyWeightDao,
    private val babyWeightToCalendarDayMapper: BabyWeightToCalendarDayMapper
) {
    suspend fun getWeightRecordOfSpecificDate(date: Int, month: Int, year: Int): CalendarDay? {
        val babyWeight = babyWeightDao.getWeightRecordForADate(date = date, month = month, year = year)
        return babyWeight?.let {
            babyWeightToCalendarDayMapper.sourceToDestination(sourceModel = it.first())
        }
    }

    suspend fun getWeightRecordOfSpecificMonth(month: Int, year: Int): List<CalendarDay>? {
        val babyWeights = babyWeightDao.getWeightRecordsForAMonth(month = month, year = year)
        return babyWeights?.let {
            babyWeightToCalendarDayMapper.sourceToDestination(sourceModel = it)
        }
    }
}
