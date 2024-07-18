package com.example.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.model.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)

abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDao
}