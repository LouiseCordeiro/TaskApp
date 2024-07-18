package com.example.todoapp.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.LocationHelper
import com.example.todoapp.databinding.AddTaskBottomSheetBinding
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.domain.model.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment(), TaskCallBack {

    @Inject
    lateinit var locationHelper: LocationHelper

    private val viewModel: TaskViewModel by viewModels()
    private var taskList: MutableList<Task> = mutableListOf()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingDialog: AddTaskBottomSheetBinding
    private lateinit var addTaskDialog: BottomSheetDialog

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var taskAdapter: TaskAdapter

    private var dueDate: Long = 0
    private lateinit var formattedDateTime: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.tasksRecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getWeatherData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            addTaskDialog.show()
        }

        observers()
        setupAddTaskDialog()
    }

    private fun setRecyclerView() {

        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter

        taskAdapter.setTaskCallBack(this)
    }

    private fun getWeatherData() {
        viewModel.getCurrentWeather(API_KEY)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getWeatherData()
            } else {
                // Permissão negada
            }
        }
    }

    override fun onTaskClick(view: View, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(taskList[position])
        this.findNavController().navigate(action)
    }

    override fun onTaskDelete(position: Int) {
        viewModel.deleteTask(taskList[position])
        taskAdapter.notifyItemRemoved(position)
    }

    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllTasks().collect {
                    taskList = it.toMutableList()
                    setRecyclerView()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weatherState.collect { weatherState ->
                    when (weatherState) {
                        is WeatherState.Loading -> {
                            binding.temperature.visibility = View.GONE
                            binding.weatherIcon.visibility = View.GONE
                        }
                        is WeatherState.Success -> {
                            binding.temperature.visibility = View.VISIBLE
                            binding.weatherIcon.visibility = View.VISIBLE
                            binding.temperature.text  = (
                                getString(R.string.celsius_temperature, weatherState.temperature)
                            )
                        }
                        is WeatherState.Error -> {
                            binding.temperature.visibility = View.GONE
                            binding.weatherIcon.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setupAddTaskDialog() {
        addTaskDialog = BottomSheetDialog(requireContext())
        bindingDialog = AddTaskBottomSheetBinding.inflate(layoutInflater)
        addTaskDialog.setContentView(bindingDialog.root,null)

        bindingDialog.calendarBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
                val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->

                val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->

                    calendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    dueDate = calendar.timeInMillis
                    formattedDateTime = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(dueDate)
                    bindingDialog.calendarBtn.text = formattedDateTime
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        bindingDialog.newTaskBtn.setOnClickListener {
            viewModel.addTask(
                Task(
                    title = bindingDialog.newTaskEditText.text.toString(),
                    date = formattedDateTime,
                    id = Random.nextInt()
                ),
            )
            bindingDialog.newTaskEditText.text.clear()
            addTaskDialog.dismiss()
        }

        addTaskDialog.setOnDismissListener {
            bindingDialog.newTaskEditText.text.clear()
            bindingDialog.calendarBtn.text = getString(R.string.select_date_reminder)
        }
    }

    companion object {
        const val DATE_PATTERN = "dd/MM/yyyy - HH:mm"
        const val API_KEY = "10e300a6921860636aabba6d44a6d28b"
    }
}