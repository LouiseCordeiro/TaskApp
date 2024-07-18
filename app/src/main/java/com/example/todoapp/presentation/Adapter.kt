package com.example.todoapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.ItemLayoutBinding
import com.example.todoapp.domain.model.Task

interface TaskCallBack {
    fun onTaskClick(view: View, position: Int)
    fun onTaskDelete(position: Int)
}

class TaskAdapter(private val taskList: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private lateinit var taskCallBack: TaskCallBack

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_layout, parent, false
        )

        return ViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task = taskList[position]

        holder.taskListBinding.task = task

        holder.setTaskCallBack(taskCallBack)
    }

    fun setTaskCallBack(taskCallBack: TaskCallBack) {
        this.taskCallBack = taskCallBack
    }

    class ViewHolder(binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener, View.OnLongClickListener {
        var taskListBinding: ItemLayoutBinding = binding

        private lateinit var taskCallBack: TaskCallBack

        init {
            taskListBinding.root.setOnClickListener(this)
            taskListBinding.root.setOnLongClickListener(this)
            taskListBinding.checkbox.setOnClickListener {
                taskCallBack.onTaskDelete(bindingAdapterPosition)
            }
        }

        fun setTaskCallBack(taskCallBack: TaskCallBack) {
            this.taskCallBack = taskCallBack
        }

        override fun onClick(v: View?) {

            if (v != null) {
                taskCallBack.onTaskClick(v, adapterPosition)
            }

        }

        override fun onLongClick(v: View?): Boolean {
            if (v != null) {
                taskCallBack.onTaskClick(v, adapterPosition)
            }
            return false
        }

    }

}