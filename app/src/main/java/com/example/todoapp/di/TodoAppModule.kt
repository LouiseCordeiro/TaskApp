package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TaskDatabase
import com.example.todoapp.data.TaskRepositoryImpl
import com.example.todoapp.domain.INotificationRepository
import com.example.todoapp.domain.IScheduleNotificationUseCase
import com.example.todoapp.domain.ScheduleNotificationUseCase
import com.example.todoapp.domain.TaskRepository
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
    fun provideTodoDao(
        taskDatabase: TaskDatabase
    ) = taskDatabase.dao

    @Singleton
    @Provides
    fun provideTodoRepository(
        dao: TaskDao
    ): TaskRepository = TaskRepositoryImpl(dao)

    @Singleton
    @Provides
    fun provideScheduleNotificationUseCase(
        repository: INotificationRepository
    ) : IScheduleNotificationUseCase = ScheduleNotificationUseCase(repository)
}