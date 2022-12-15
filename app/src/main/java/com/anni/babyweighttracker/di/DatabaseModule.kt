package com.anni.babyweighttracker.di

import android.content.Context
import androidx.room.Room
import com.anni.babyweighttracker.data.AppDatabase
import com.anni.babyweighttracker.data.weight.BabyWeightDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "baby-weight-tracker").build()

    @Provides
    fun provideBabyWeightDao(appDatabase: AppDatabase): BabyWeightDao = appDatabase.babyWeightDao()
}
