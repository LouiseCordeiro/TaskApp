package com.example.todoapp.domain

interface INotificationRepository {
    fun scheduleNotification(
        taskId: Int,
        notificationTime: Long,
        title: String
    )
}