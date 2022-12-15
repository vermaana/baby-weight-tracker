package com.anni.babyweighttracker.data.weight

import androidx.room.Dao
import androidx.room.Query
import com.anni.babyweighttracker.data.weight.BabyWeight

@Dao
interface BabyWeightDao {
    @Query("SELECT * FROM baby_weight_record WHERE entry_month IN (:month) AND entry_year IN (:year)")
    suspend fun getWeightRecordsByMonthAndYear(month: Int, year: Int): List<BabyWeight>?
}
