package com.example.todoapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.data.model.Task
import com.example.todoapp.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : Fragment() {

    private val navigationArgs: EditFragmentArgs by navArgs()
    private val viewModel: TodoViewModel by viewModels()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private var action = EditFragmentDirections.actionEditFragmentToHomeFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setComponents(navigationArgs.task)
        val task: Task? = arguments?.getParcelable(ARG_TASK)
        if (task != null) {
            // Utilize os dados da tarefa para preencher o fragmento
            binding.editTask.setText(task.title)
            // Continue preenchendo outros campos conforme necessário
        }
    }

    private fun setComponents(task: Task) {
        binding.editTask.setText(task.title)
        binding.saveButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateTask(
                    Task(
                        id = task.id,
                        title = binding.editTask.text.toString()
                    )
                )
            }
            this.findNavController().navigate(action)
        }

        binding.deleteButton.setOnClickListener {
            viewModel.deleteTask(task)
            this.findNavController().navigate(action)
        }

    }

    companion object {
        private const val ARG_TASK = "task"

        fun newInstance(task: Task): EditFragment {
            val fragment = EditFragment()
            val args = Bundle()
            args.putParcelable(ARG_TASK, task)
            fragment.arguments = args
            return fragment
        }
    }

}