package com.example.android.tika.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.R
import com.example.android.tika.data.database.Activity
import com.example.android.tika.data.database.Task
import com.example.android.tika.data.presentation.*

import com.example.android.tika.databinding.DailyActivityItemBinding
import com.example.android.tika.utils.flatMapTaskObject
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

interface NotifyItemChangedListener {
    fun notifyViewToggle(position: Int)
}

class ActivityAdapter() :
    ListAdapter<ActivityItem, ActivityAdapter.ActivityViewHolder>(ActivityDiffCallBack()),
    NotifyItemChangedListener {

    private var pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    var scope: CoroutineScope? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        return ActivityViewHolder.from(parent, pool, scope)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item: ActivityItem = getItem(position)
        holder.bind(item)
        holder.setNotifyItemChangedListener(this)
    }

    class ActivityDiffCallBack : DiffUtil.ItemCallback<ActivityItem>() {
        override fun areItemsTheSame(old: ActivityItem, new: ActivityItem): Boolean {
            return old.activityId == new.activityId
        }

        override fun areContentsTheSame(old: ActivityItem, new: ActivityItem): Boolean {
            return old == new
        }
    }

    override fun notifyViewToggle(position: Int) {
        notifyItemChanged(position)
    }

    class ActivityViewHolder(
        private val binding: DailyActivityItemBinding,
        private val pool: RecyclerView.RecycledViewPool,
        private val scope: CoroutineScope?)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val context = binding.root.context
        private val res = context.resources
        private lateinit var item: ActivityItem
        private lateinit var listener: NotifyItemChangedListener

        init {
            binding.activityView.setOnClickListener(this)
        }

        fun bind(item: ActivityItem) {
            this.item = item

            binding.apply {
                scope?.launch {
                    val count = numberOfComments()
                    initialize(count)
                    setupNestedRecyclerView(count)
                    reconfigure()
                    executePendingBindings()
                }
            }
        }

        companion object {

            val ONE_DAY_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

            fun from(parent: ViewGroup, pool: RecyclerView.RecycledViewPool, scope: CoroutineScope?)
                    : ActivityViewHolder {
                val inflater: LayoutInflater = LayoutInflater.from(parent.context)
                val itemBinding: DailyActivityItemBinding =
                    DailyActivityItemBinding.inflate(inflater, parent, false)

                return ActivityViewHolder(itemBinding, pool, scope)
            }
        }

        fun setNotifyItemChangedListener(listener: NotifyItemChangedListener) {
            this.listener = listener
        }

        private suspend fun numberOfComments(): Int {
            return withContext(Dispatchers.IO) {
                item.totalNumberOfComments(binding.root.context)
            }
        }

        /**
         * Sets up the date, progress and total number of comments per [Activity]
         * per day
         */
        private fun initialize(count: Int) {
            val time = System.currentTimeMillis() - item.date.time

            binding.dayText.text = when {
                (time < ONE_DAY_MILLIS) -> res.getString(R.string.today)
                (time < (ONE_DAY_MILLIS * 2)) -> res.getString(R.string.yesterday)
                else -> {
                    binding.dayText.visibility = View.GONE; null
                }
            }

            binding.dateText.text = item.formatDate()
            if (item.tasks.isNotEmpty() && count > 0) {
                binding.emptyActivityView.visibility = View.GONE
                binding.activityView.visibility = View.VISIBLE
                binding.ratioCompleted.text =
                    res.getString(R.string.progress_ratio, item.numberOfTasksCompleted(), item.tasks.size)
                binding.commentsText.text = res.getString(R.string.num_comments, count)
            }
        }

        /**
         * Creates the nested recycler view for the list of [Task] nested within an [Activity]
         * @param count the number of comments within a [Task]
         */
        private fun setupNestedRecyclerView(count: Int) {
            val layoutManager = LinearLayoutManager(
                binding.tasksCommentsRecyclerView.context,
                LinearLayoutManager.VERTICAL, false
            )
            layoutManager.initialPrefetchItemCount = count + item.tasks.size

            val adapter = TaskWithCommentAdapter(scope)
            binding.tasksCommentsRecyclerView.adapter = adapter
            binding.tasksCommentsRecyclerView.layoutManager = layoutManager
            binding.tasksCommentsRecyclerView.setRecycledViewPool(pool)

            // Create sub-item view adapter
            scope?.launch {
                val result = flatMap()
                adapter.submitList(result)
            }
        }

        private suspend fun flatMap(): MutableList<Any> {
            return withContext(Dispatchers.IO) {
                flatMapTaskObject(context, item.tasks)

            }
        }

        /**
         *  Logic used to control the collapsible layout
         * @param activity an [Activity] per day for the [User]
         */
        private fun reconfigure() {
            binding.tasksCommentsRecyclerView.visibility = if (item.expanded) {
                binding.panel.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.light_gray_background))
                View.VISIBLE
            } else {
                binding.panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                View.GONE
            }
        }

        override fun onClick(v: View?) {
            item.expanded = !item.expanded
            listener.notifyViewToggle(adapterPosition)
        }
    }
}