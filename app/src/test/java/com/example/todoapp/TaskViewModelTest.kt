package com.example.todoapp

import com.example.todoapp.data.model.Task
import com.example.todoapp.domain.usecases.IAddTaskUseCase
import com.example.todoapp.domain.usecases.IDeleteTaskUseCase
import com.example.todoapp.domain.usecases.IGetAllTasksUseCase
import com.example.todoapp.domain.usecases.IGetCurrentWeatherUseCase
import com.example.todoapp.domain.usecases.IGetLocationUseCase
import com.example.todoapp.domain.usecases.IUpdateTaskUseCase
import com.example.todoapp.presentation.TaskViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class TaskViewModelTest {

    private val getCurrentWeatherUseCase = mockk<IGetCurrentWeatherUseCase>()
    private val getLocationUseCase = mockk<IGetLocationUseCase>()
    private val addTaskUseCase = mockk<IAddTaskUseCase>()
    private val deleteTaskUseCase = mockk<IDeleteTaskUseCase>()
    private val updateTaskUseCase = mockk<IUpdateTaskUseCase>()
    private val getAllTasksUseCase = mockk<IGetAllTasksUseCase>()

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        viewModel = TaskViewModel(
            getCurrentWeatherUseCase,
            getLocationUseCase,
            addTaskUseCase,
            deleteTaskUseCase,
            updateTaskUseCase,
            getAllTasksUseCase
        )

    }

    @Test
    fun `getTasks should return tasks from repository`() = runTest {
        val tasks = listOf( Task(0, "Test"))
        every { getAllTasksUseCase() } returns flowOf(tasks)

        val result = viewModel.getTasks()

        result.collect {
            assertEquals(tasks, it)
        }

        verify { getAllTasksUseCase() }
    }


    @Test
    fun `should add tasks`() = runTest {
        val task = Task(0, "Test")
        coEvery { addTaskUseCase(task) } just Runs

        viewModel.addTask(task)

        coVerify { addTaskUseCase(task) }
    }

    @Test
    fun `updateTask should call update from UpdateUseCase`() = runTest {
        val task = Task(0, "Test")
        coEvery { updateTaskUseCase(task) } just Runs

        viewModel.updateTask(task)

        coVerify { updateTaskUseCase(task) }
    }

    @Test
    fun `deleteTask should call delete from repository`() = runTest {
        val task = Task(0, "Test")
        coEvery { deleteTaskUseCase(task) } just Runs

        viewModel.deleteTask(task)

        coVerify { deleteTaskUseCase(task) }
    }

}