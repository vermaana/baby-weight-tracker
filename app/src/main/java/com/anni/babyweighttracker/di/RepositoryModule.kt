package com.anni.babyweighttracker.di

import com.anni.babyweighttracker.calendar.repository.CalendarRepository
import com.anni.babyweighttracker.data.weight.BabyWeightDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideCalendarRepository(babyWeightDao: BabyWeightDao): CalendarRepository = CalendarRepository(babyWeightDao = babyWeightDao)
}
