package com.anni.babyweighttracker.calendar.repository

import com.anni.babyweighttracker.data.weight.BabyWeight
import com.anni.babyweighttracker.data.weight.BabyWeightDao
import javax.inject.Inject

class CalendarRepository @Inject constructor(
    private val babyWeightDao: BabyWeightDao
) {
    suspend fun getWeightRecordOfSpecificMonth(month: Int, year: Int): List<BabyWeight>? = babyWeightDao.getWeightRecordsByMonthAndYear(month = month, year = year)
}
