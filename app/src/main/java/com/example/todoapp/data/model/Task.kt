package com.example.todoapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class Task(
    @PrimaryKey(true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "notificationTime") var date: String = "",
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false
): Parcelable