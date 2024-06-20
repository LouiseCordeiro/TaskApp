package com.example.todoapp.domain

interface IScheduleNotificationUseCase {
    fun invoke(
        taskId: Int,
        notificationTime: Long,
        title: String
    )
}