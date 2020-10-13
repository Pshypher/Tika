package com.example.android.tika.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.R
import com.example.android.tika.data.presentation.TaskAdapterItem
import com.example.android.tika.databinding.TaskItemBinding

class TaskAdapter(private val tasks: MutableList<TaskAdapterItem>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    class TaskDiffCallBack(private val newItems: List<TaskAdapterItem>,
                           private val oldItems: List<TaskAdapterItem>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size ?: 0

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old.taskId == new.taskId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }

    class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TaskAdapterItem) {
            binding.task = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: TaskItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.task_item, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) =
        holder.bind(tasks[position])

    override fun getItemCount(): Int = tasks.size

    fun swap(tasks: List<TaskAdapterItem>) {
        val diffCallBack = TaskDiffCallBack(tasks, this.tasks)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.tasks.clear()
        this.tasks.addAll(tasks)

        // calls the adapter's notify methods after diff is calculated
        diffResult.dispatchUpdatesTo(this)
    }
}