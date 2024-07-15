package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.LocationHelper
import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.database.TaskDatabase
import com.example.todoapp.data.repositories.TaskRepositoryImpl
import com.example.todoapp.data.remote.WeatherApiService
import com.example.todoapp.data.repositories.WeatherRepositoryImpl
import com.example.todoapp.domain.ITaskRepository
import com.example.todoapp.domain.IWeatherRepository
import com.example.todoapp.domain.usecases.AddTaskUseCase
import com.example.todoapp.domain.usecases.DeleteTaskUseCase
import com.example.todoapp.domain.usecases.GetAllTasksUseCase
import com.example.todoapp.domain.usecases.GetCurrentWeatherUseCase
import com.example.todoapp.domain.usecases.GetLocationUseCase
import com.example.todoapp.domain.usecases.IAddTaskUseCase
import com.example.todoapp.domain.usecases.IDeleteTaskUseCase
import com.example.todoapp.domain.usecases.IGetAllTasksUseCase
import com.example.todoapp.domain.usecases.IGetCurrentWeatherUseCase
import com.example.todoapp.domain.usecases.IGetLocationUseCase
import com.example.todoapp.domain.usecases.IUpdateTaskUseCase
import com.example.todoapp.domain.usecases.UpdateTaskUseCase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TodoAppModule {

    @Singleton
    @Provides
    fun provideTodoDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        TaskDatabase::class.java,
        "todo_database"
    ).build()

    @Singleton
    @Provides
    fun provideTaskDao(
        taskDatabase: TaskDatabase
    ) = taskDatabase.dao

    @Singleton
    @Provides
    fun provideTaskRepository(
        dao: TaskDao
    ): ITaskRepository = TaskRepositoryImpl(dao)

    @Singleton
    @Provides
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(
        weatherApiService: WeatherApiService,
        crashlytics: FirebaseCrashlytics
    ): IWeatherRepository = WeatherRepositoryImpl(weatherApiService, crashlytics)

    @Provides
    @Singleton
    fun provideGetAllTasks(repository: ITaskRepository) : IGetAllTasksUseCase {
        return GetAllTasksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddTaskUseCase(repository: ITaskRepository): IAddTaskUseCase {
        return AddTaskUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repository: ITaskRepository): IDeleteTaskUseCase {
        return DeleteTaskUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(repository: ITaskRepository): IUpdateTaskUseCase {
        return UpdateTaskUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentWeatherUseCase(repository: IWeatherRepository): IGetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLocationUseCase(locationHelper: LocationHelper) : IGetLocationUseCase {
        return GetLocationUseCase(locationHelper)
    }

}