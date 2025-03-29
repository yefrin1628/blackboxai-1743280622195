package com.example.simuladorventas.di

import android.content.Context
import com.example.simuladorventas.datos.database.AppDatabase
import com.example.simuladorventas.datos.repository.SalesRepository
import com.example.simuladorventas.datos.repository.GoalsRepository
import com.example.simuladorventas.datos.dao.SaleDao
import com.example.simuladorventas.datos.dao.GoalDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideSaleDao(database: AppDatabase): SaleDao = database.saleDao()

    @Provides
    fun provideGoalDao(database: AppDatabase): GoalDao = database.goalDao()

    @Provides
    @Singleton
    fun provideSalesRepository(saleDao: SaleDao): SalesRepository {
        return SalesRepository(saleDao)
    }

    @Provides
    @Singleton
    fun provideGoalsRepository(goalDao: GoalDao): GoalsRepository {
        return GoalsRepository(goalDao)
    }
}