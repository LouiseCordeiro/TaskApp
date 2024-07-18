package com.example.todoapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.domain.model.Task

@Entity(tableName = "todo_table")
data class TaskEntity(
    @PrimaryKey(false) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "notificationTime") var date: String = "",
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false
)

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    date = date,
    isCompleted = isCompleted
)

fun TaskEntity.toTask() = Task(
    id = id,
    title = title,
    date = date,
    isCompleted = isCompleted
)