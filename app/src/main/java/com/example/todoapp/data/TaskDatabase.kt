package com.example.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.model.Task

@Database(
    entities = [Task::class],
    version = 1
)

abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDao
}