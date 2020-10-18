package com.example.android.tika.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.R
import com.example.android.tika.data.presentation.TaskItem
import com.example.android.tika.databinding.TaskItemBinding

class TaskAdapter() : ListAdapter<TaskItem, TaskAdapter.TaskViewHolder>(TaskDiffCallBack()) {

    class TaskDiffCallBack : DiffUtil.ItemCallback<TaskItem>() {
        override fun areItemsTheSame(old: TaskItem, new: TaskItem): Boolean {
            return old.taskId == new.taskId
        }

        override fun areContentsTheSame(old: TaskItem, new: TaskItem): Boolean {
            return old == new
        }
    }

    class TaskViewHolder private constructor(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskItem) {
            binding.task = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: TaskItemBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.task_item, parent, false)
                return TaskViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}