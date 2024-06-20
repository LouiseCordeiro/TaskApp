package com.example.todoapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.model.Task
import com.example.todoapp.databinding.ItemLayoutBinding

class Adapter(
    private val onTaskClicked: (Task) -> Unit,
    private val onCheckBoxClicked: (Task) -> Unit
) : ListAdapter<Task, Adapter.ViewHolder>(DiffCallback) {

    private lateinit var binding: ItemLayoutBinding

    inner class ViewHolder(binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(task: Task) {
                binding.apply {
                    taskTitle.text = task.title
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTask: Task = getItem(position)

        holder.itemView.setOnClickListener {
            onTaskClicked(currentTask)
        }

        binding.checkbox.setOnClickListener {
            onCheckBoxClicked(currentTask)
        }

        holder.bind(currentTask)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}