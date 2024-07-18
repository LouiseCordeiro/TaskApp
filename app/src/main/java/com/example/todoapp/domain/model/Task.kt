package com.example.todoapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Int,
    val title: String,
    var date: String = "",
    var isCompleted: Boolean = false
) : Parcelable
