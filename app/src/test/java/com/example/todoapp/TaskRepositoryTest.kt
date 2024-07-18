package com.example.todoapp

import com.example.todoapp.data.database.TaskDao
import com.example.todoapp.data.repositories.TaskRepositoryImpl
import com.example.todoapp.data.model.TaskEntity
import com.example.todoapp.data.model.toEntity
import com.example.todoapp.data.model.toTask
import com.example.todoapp.domain.model.Task
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {

    private var dao = mockk<TaskDao>()
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setup(){
        repository = TaskRepositoryImpl(dao)
    }

    @Test
    fun `should get all tasks from room database`() = runTest {
        val tasks = listOf(Task(id = 0, title= "teste"))
        every { dao.getAllTasks() } returns flowOf(tasks.map { it.toEntity() })

       val result = repository.getAllTasks()

        result.collect {
            assertEquals(tasks, it)
        }

        verify { dao.getAllTasks() }
    }

    @Test
    fun `should add task to room database`() = runTest {
        val taskEntity = TaskEntity(id = 0 , title = "teste")
        coEvery { dao.add(taskEntity) }  just Runs

        repository.add(taskEntity.toTask())

        coVerify { dao.add(taskEntity) }
    }

    @Test
    fun `should update task in room databe`() = runTest {
        val taskEntity = TaskEntity(id = 1, "teste")
        coEvery { dao.update(taskEntity) } just Runs

        repository.update(taskEntity.toTask())

        coVerify { dao.update(taskEntity) }
    }

    @Test
    fun `should delete task in room database`() = runTest {
        val taskEntity = TaskEntity(id = 0, "teste")
        coEvery { dao.delete(taskEntity) } just Runs

        repository.delete(taskEntity.toTask())

        coVerify { dao.delete(taskEntity) }
    }

}