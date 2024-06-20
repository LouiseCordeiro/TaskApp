package com.example.todoapp.domain

import javax.inject.Inject

class ScheduleNotificationUseCase @Inject constructor(
    private val notificationRepository: INotificationRepository
) : IScheduleNotificationUseCase {

    override operator fun invoke(
        taskId: Int,
        notificationTime: Long,
        title: String,
    ) {
        notificationRepository.scheduleNotification(taskId, notificationTime, title)
    }
}