package com.anni.babyweighttracker.data.weight

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baby_weight_record")
data class BabyWeight(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "weight_in_grams") val weightInGrams: Int,
    @ColumnInfo(name = "entry_date") val entryDate: Int,
    @ColumnInfo(name = "entry_month") val entryMonth: Int,
    @ColumnInfo(name = "entry_year") val entryYear: Int
)
