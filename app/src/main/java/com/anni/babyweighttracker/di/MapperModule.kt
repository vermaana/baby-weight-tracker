package com.anni.babyweighttracker.di

import com.anni.babyweighttracker.mapper.BabyWeightToCalendarDayMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    fun provideBabyWeightToCalendarDayMapper(): BabyWeightToCalendarDayMapper = BabyWeightToCalendarDayMapper()
}
