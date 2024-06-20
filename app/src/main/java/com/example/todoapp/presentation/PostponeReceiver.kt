package com.example.todoapp.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todoapp.data.TaskRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostponeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepositoryImpl

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("TASK_ID", -1)
        if (taskId == -1) return

        CoroutineScope(Dispatchers.IO).launch {
            taskRepository.getTask(taskId).collect { task ->
                task?.let {
                    val newDueDate = System.currentTimeMillis() + 30 * 60 * 1000 // Adia 30 minutos
                    it.notificationTime = newDueDate
                    taskRepository.update(it)
                    NotificationScheduler.scheduleTaskReminder(context, it)
                }
            }
        }
    }
}