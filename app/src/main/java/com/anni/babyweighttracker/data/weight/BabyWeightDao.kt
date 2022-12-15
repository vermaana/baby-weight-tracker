package com.anni.babyweighttracker.data.weight

import androidx.room.Dao
import androidx.room.Query

@Dao
interface BabyWeightDao {
    @Query("SELECT * FROM baby_weight_record WHERE entry_date IN (:date) AND entry_month IN (:month) AND entry_year IN (:year)")
    suspend fun getWeightRecordForADate(date: Int, month: Int, year: Int): List<BabyWeight>?

    @Query("SELECT * FROM baby_weight_record WHERE entry_month IN (:month) AND entry_year IN (:year)")
    suspend fun getWeightRecordsForAMonth(month: Int, year: Int): List<BabyWeight>?
}
