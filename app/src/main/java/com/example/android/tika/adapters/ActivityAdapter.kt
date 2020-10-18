package com.example.android.tika.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.R
import com.example.android.tika.data.database.Activity
import com.example.android.tika.data.database.Task
import com.example.android.tika.data.presentation.*

import com.example.android.tika.databinding.DailyActivityItemBinding
import com.example.android.tika.utils.flatMapTaskObject
import kotlinx.coroutines.*

interface NotifyItemChangedListener {
    fun notifyViewToggle(position: Int)
}

class ActivityAdapter() :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>(), NotifyItemChangedListener {

    private var pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    var scope: CoroutineScope? = null
        set(value) {
            field = value
        }

    var items = listOf<ActivityItem>()
        set(value) {
            val diffCallBack = ActivityDiffCallBack(value, items)
            val diffResult = DiffUtil.calculateDiff(diffCallBack)
            field = value

            // calls the adapter's notify methods after diff is calculated
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: DailyActivityItemBinding =
            DailyActivityItemBinding.inflate(inflater, parent, false)

        return ActivityViewHolder(itemBinding, pool, scope)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item: ActivityItem = items[position]
        holder.bind(item)
        holder.setNotifyItemChangedListener(this)
    }

    override fun getItemCount(): Int = items.size

    class ActivityDiffCallBack(
        private val newItems: List<ActivityItem>,
        private val oldItems: List<ActivityItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old.activityId == new.activityId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }

    override fun notifyViewToggle(position: Int) {
        notifyItemChanged(position)
    }

    class ActivityViewHolder(
        private val binding: DailyActivityItemBinding,
        private val pool: RecyclerView.RecycledViewPool,
        private val scope: CoroutineScope?
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val context = binding.root.context
        private val res = context.resources
        private lateinit var item: ActivityItem
        private lateinit var listener: NotifyItemChangedListener

        init {
            binding.activityView.setOnClickListener(this)
        }

        fun setNotifyItemChangedListener(listener: NotifyItemChangedListener) {
            this.listener = listener
        }

        fun bind(item: ActivityItem) {
            this.item = item

            binding.apply {
                scope?.launch {
                    val count = numberOfComments()
                    init(count)
                    setupNestedRecyclerView(count)
                    reconfigure()
                    executePendingBindings()
                }
            }
        }

        suspend fun DailyActivityItemBinding.numberOfComments(): Int {
            return withContext(Dispatchers.IO) {
                item.totalNumberOfComments(root.context)
            }
        }

        /**
         * Sets up the date, progress and total number of comments per [Activity]
         * per day
         */
        private fun DailyActivityItemBinding.init(count: Int) {
            val millisecondsPerDay = 1000 * 60 * 60 * 24
            val time = System.currentTimeMillis() - item.date.time
            dayText.text = when {
                (time < millisecondsPerDay) -> res.getString(R.string.today)
                (time < (millisecondsPerDay * 2)) -> res.getString(R.string.yesterday)
                else -> {
                    dayText.visibility = View.GONE; null
                }
            }

            dateText.text = item.formatDate()

            if (item.tasks.isNotEmpty() && count > 0) {
                emptyActivityView.visibility = View.GONE; activityView.visibility = View.VISIBLE
                ratioCompleted.text =
                    res.getString(
                        R.string.progress_ratio,
                        item.numberOfTasksCompleted(),
                        item.tasks.size
                    )
                commentsText.text = res.getString(R.string.num_comments, count)
            }

        }

        /**
         * Creates the nested recycler view for the list of [Task] nested within an [Activity]
         * @param count the number of comments within a [Task]
         */
        private fun DailyActivityItemBinding.setupNestedRecyclerView(count: Int) {
            val layoutManager = LinearLayoutManager(
                tasksCommentsRecyclerView.context,
                LinearLayoutManager.VERTICAL, false
            )
            layoutManager.initialPrefetchItemCount = count + item.tasks.size

            val adapter = TaskWithCommentAdapter(scope)
            tasksCommentsRecyclerView.adapter = adapter
            tasksCommentsRecyclerView.layoutManager = layoutManager
            tasksCommentsRecyclerView.setRecycledViewPool(pool)

            // Create sub-item view adapter
            scope?.launch {
                val result = flatMap()
                adapter.submitList(result)
            }
        }

        suspend fun flatMap(): MutableList<Any> {
            return withContext(Dispatchers.IO) {
                flatMapTaskObject(context, item.tasks)

            }
        }

        /**
         *  Logic used to control the collapsible layout
         * @param activity an [Activity] per day for the [User]
         */
        private fun DailyActivityItemBinding.reconfigure() {
            tasksCommentsRecyclerView.visibility = if (item.expanded) {
                panel.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light_gray_background
                    )
                )
                View.VISIBLE
            } else {
                panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                View.GONE
            }
        }

        override fun onClick(v: View?) {
            item.expanded = !item.expanded
            listener.notifyViewToggle(adapterPosition)
        }
    }
}