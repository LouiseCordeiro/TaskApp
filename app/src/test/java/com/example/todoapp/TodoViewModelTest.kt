package com.example.todoapp

import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.TaskRepository
import com.example.todoapp.presentation.TodoViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TodoViewModelTest {

    @MockK
    private lateinit var taskRepository: TaskRepository

    @MockK
    private lateinit var application: TodoApplication

    private lateinit var viewModel: TodoViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = TodoViewModel(taskRepository, application)
    }

    @Test
    fun `getTasks should return tasks from repository`() = runTest {
        val tasks = listOf( Task(0, "Test"))
        every { taskRepository.getAllTasks() } returns flowOf(tasks)

        val result = viewModel.getTasks()

        result.collect {
            assertEquals(tasks, it)
        }

        verify { taskRepository.getAllTasks() }
    }

    @Test
    fun `addTask should call add from repository`() = runTest {
        val task = Task(0, "Test")
        coEvery { taskRepository.add(task) } just Runs

        viewModel.addTask(task)

        coVerify { taskRepository.add(task) }
    }

    @Test
    fun `updateTask should call uptade from repository`() = runTest {
        val task = Task(0, "Test")
        coEvery { taskRepository.update(task) } just Runs

        viewModel.updateTask(task)

        coVerify { taskRepository.update(task) }
    }

    @Test
    fun `deleteTask should call delete from repository`() = runTest {
        val task = Task(0, "Test")
        coEvery { taskRepository.delete(task) } just Runs

        viewModel.deleteTask(task)

        coVerify { taskRepository.delete(task) }
    }

}