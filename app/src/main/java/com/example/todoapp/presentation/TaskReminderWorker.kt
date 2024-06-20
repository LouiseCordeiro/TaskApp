package com.example.todoapp.presentation

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todoapp.R
import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val taskId = inputData.getInt("TASK_ID", -1)
        if (taskId == -1) return Result.failure()
        lateinit var task: Task

        taskRepository.getTask(taskId).collect {
            task = it
        }

        val now = System.currentTimeMillis()

        if (task.notificationTime > now) {
            showNotification(task)
            return Result.success()
        } else {
            return Result.failure()
        }

    }

    private fun showNotification(task: Task) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("TASK", task)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            task.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "task_channel")
            .setContentTitle("Lembrete de Tarefa")
            .setContentText(task.title)
            .setSmallIcon(R.drawable.google_tasks)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(task.id, notification)

        WorkManager.getInstance(applicationContext).cancelAllWorkByTag(task.id.toString())
    }
}
