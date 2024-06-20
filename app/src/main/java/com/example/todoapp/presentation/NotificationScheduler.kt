package com.example.todoapp.presentation

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todoapp.data.model.Task
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleTaskReminder(context: Context, task: Task) {
        val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
            .setInputData(workDataOf("TASK_ID" to task.id))
            .setInitialDelay(getDelay(task.notificationTime), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun getDelay(dueDate: Long): Long {
        val now = System.currentTimeMillis()
        return if (dueDate > now) dueDate - now else 0
    }
}
