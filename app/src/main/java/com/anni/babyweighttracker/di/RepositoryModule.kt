package com.anni.babyweighttracker.di

import com.anni.babyweighttracker.calendar.repository.CalendarRepository
import com.anni.babyweighttracker.data.weight.BabyWeightDao
import com.anni.babyweighttracker.mapper.BabyWeightToCalendarDayMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideCalendarRepository(babyWeightDao: BabyWeightDao, babyWeightToCalendarDayMapper: BabyWeightToCalendarDayMapper): CalendarRepository =
        CalendarRepository(babyWeightDao = babyWeightDao, babyWeightToCalendarDayMapper = babyWeightToCalendarDayMapper)
}
