package com.example.todoapp.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.model.Task
import com.example.todoapp.databinding.AddTaskBottomSheetBinding
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment(), TaskCallBack {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: TodoViewModel by viewModels()
    private var taskList: MutableList<Task> = mutableListOf()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingDialog: AddTaskBottomSheetBinding
    private lateinit var addTaskDialog: BottomSheetDialog

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var taskAdapter: TaskAdapter

    private var dueDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerView = binding.tasksRecyclerView
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        viewModel.getTasks()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            addTaskDialog.show()
        }

        observer()
        setupAddTaskDialog()
    }

    private fun setRecyclerView() {

        taskAdapter = TaskAdapter(taskList)
        recyclerView.adapter = taskAdapter

        taskAdapter.setTaskCallBack(this)

    }

    override fun onTaskClick(view: View, position: Int, isLongClick: Boolean) {

        if (isLongClick) {
            Log.e(TAG, "Position: $position is a long click")
        } else {

            Log.e(TAG, "Position: $position is a single click")
        }

    }

    override fun onTaskDelete(position: Int) {
        viewModel.deleteTask(taskList[position])
        taskAdapter.notifyItemRemoved(position)
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getTasks().collect {
                    taskList = it.toMutableList()
                    setRecyclerView()
                }
            }
        }
    }

    private fun setupAddTaskDialog() {
        addTaskDialog = BottomSheetDialog(requireContext())
        bindingDialog = AddTaskBottomSheetBinding.inflate(layoutInflater)
        addTaskDialog.setContentView(bindingDialog.root,null)

        bindingDialog.calendarBtn.setOnClickListener {
                val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->

                val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->

                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    dueDate = calendar.timeInMillis
                    val formattedDateTime = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault()).format(dueDate)
                    bindingDialog.calendarBtn.text = formattedDateTime
                }, 0, 0, true)
                timePicker.show()
            }, 2024, 5, 15)
            datePicker.show()
        }

        bindingDialog.newTaskBtn.setOnClickListener {
            viewModel.addTask(
                Task(
                    title = bindingDialog.newTaskEditText.text.toString(),
                    date = dueDate,
                    id = taskList.size + 1
                ),
            )
            bindingDialog.newTaskEditText.text.clear()
            addTaskDialog.dismiss()
        }

        addTaskDialog.setOnDismissListener {
            bindingDialog.newTaskEditText.text.clear()
        }
    }
}