package com.anni.babyweighttracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anni.babyweighttracker.data.weight.BabyWeightRecord
import com.anni.babyweighttracker.data.weight.BabyWeightDao

@Database(entities = [BabyWeightRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun babyWeightDao(): BabyWeightDao
}
