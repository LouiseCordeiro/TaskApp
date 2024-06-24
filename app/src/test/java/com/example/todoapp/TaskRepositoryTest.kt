package com.example.todoapp

import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TaskRepositoryImpl
import com.example.todoapp.data.model.Task
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {

    @MockK
    private lateinit var dao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setup(){
        repository = TaskRepositoryImpl(dao)
    }

    @Test
    fun `should get all tasks from room database`() = runTest {
        val tasks = listOf(Task(id = 0, title= "teste"))
        every { dao.getAllTasks() } returns flowOf(tasks)

       val result = repository.getAllTasks()

        result.collect {
            assertEquals(tasks, it)
        }

        verify { dao.getAllTasks() }
    }

    @Test
    fun `should add task to room database`() = runTest {
        val task = Task(id = 0 , title = "teste")
        coEvery { dao.add(task) }  just Runs

        repository.add(task)

        coVerify { dao.add(task) }
    }

    @Test
    fun `should get task by id from room database`() = runTest {
        val task = Task(id= 1, title = "teste")
        coEvery { dao.getTask(0) } returns flowOf(task)

        val result = repository.getTask(0)

        result.collect {
            assertEquals(task, it   )
        }

        coVerify { dao.getTask(0) }
    }

    @Test
    fun `should update task in room databe`() = runTest {
        val task = Task(id = 1, "teste")
        coEvery { dao.update(task) } just Runs

        repository.update(task)

        coVerify { dao.update(task) }
    }

    @Test
    fun `should delete task in room database`() = runTest {
        val task = Task(id = 0, "teste")
        coEvery { dao.delete(task) } just Runs

        repository.delete(task)

        coVerify { dao.delete(task) }
    }

}