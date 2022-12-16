package com.anni.babyweighttracker.data.weight

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BabyWeightDao {
    @Query("SELECT * FROM baby_weight_record WHERE entry_date IN (:date) AND entry_month IN (:month) AND entry_year IN (:year)")
    suspend fun getWeightRecordsForADate(date:Int, month: Int, year: Int): List<BabyWeightRecord>

    @Query("SELECT * FROM baby_weight_record WHERE entry_month IN (:month) AND entry_year IN (:year)")
    suspend fun getWeightRecordsForAMonth(month: Int, year: Int): List<BabyWeightRecord>

    @Insert(entity = BabyWeightRecord::class, onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWeightRecordForADate(babyWeightRecord: BabyWeightRecord): Long

    @Update(entity = BabyWeightRecord::class)
    suspend fun updateWeightRecordForADate(babyWeightRecord: BabyWeightRecord): Int
}
