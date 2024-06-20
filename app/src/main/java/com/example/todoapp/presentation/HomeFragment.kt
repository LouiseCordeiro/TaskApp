package com.example.todoapp.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.data.model.Task
import com.example.todoapp.databinding.AddTaskBottomSheetBinding
import com.example.todoapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingDialog: AddTaskBottomSheetBinding
    private val viewModel: TodoViewModel by viewModels()
    private lateinit var addTaskDialog: BottomSheetDialog
    private val calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
    private var dueDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bindingDialog = AddTaskBottomSheetBinding.inflate(inflater, container, false)
        viewModel.getTasks()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            addTaskDialog.show()
        }
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val adapter = Adapter(
            { task ->
                val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(task)
                this.findNavController().navigate(action)
            },{ task ->
                viewModel.deleteTask(task)
            }
        )

        binding.tasksRecyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getTasks().collect {
                    adapter.submitList(it)
                }
            }
        }
        setupAddTaskDialog()
    }

    private fun setupAddTaskDialog() {
        addTaskDialog = BottomSheetDialog(requireContext())
        bindingDialog = AddTaskBottomSheetBinding.inflate(layoutInflater)
        addTaskDialog.setContentView(bindingDialog.root,null)

        bindingDialog.calendarBtn.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                // Após selecionar a data, mostrar TimePickerDialog
                val timePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                    // Combinar data e hora
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
                    // Agora você pode usar o dueDate
                }, 0, 0, true)
                timePicker.show()
            }, 2024, 5, 15)
            datePicker.show()
        }

        bindingDialog.newTaskBtn.setOnClickListener {
            viewModel.addTask(
                Task(
                    title = bindingDialog.newTaskEditText.text.toString(),
                    notificationTime = dueDate,
                    id = Random.nextInt()
                ),
            )
            bindingDialog.newTaskEditText.text.clear()
            addTaskDialog.dismiss()
        }
    }
}