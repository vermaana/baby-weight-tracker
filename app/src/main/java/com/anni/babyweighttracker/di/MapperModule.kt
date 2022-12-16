package com.anni.babyweighttracker.di

import com.anni.babyweighttracker.mapper.BabyWeightRecordToCalendarDayMapper
import com.anni.babyweighttracker.mapper.CalendarDayToBabyWeightRecordMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    fun provideBabyWeightRecordToCalendarDayMapper(): BabyWeightRecordToCalendarDayMapper = BabyWeightRecordToCalendarDayMapper()

    @Provides
    fun provideCalendarDayToBabyWeightRecordMapper(): CalendarDayToBabyWeightRecordMapper = CalendarDayToBabyWeightRecordMapper()
}
