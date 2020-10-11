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
import com.example.android.tika.data.presentation.ActivityAdapterItem
import com.example.android.tika.data.presentation.formatDate
import com.example.android.tika.data.presentation.numberOfTasksCompleted
import com.example.android.tika.data.presentation.totalNumberOfComments

import com.example.android.tika.databinding.DailyActivityItemBinding
import com.example.android.tika.utils.flatMapTaskObject
import kotlinx.coroutines.*

interface NotifyItemChangedListener {
    fun notifyViewToggle(position: Int)
}

class ActivityAdapter(private var items: MutableList<ActivityAdapterItem>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>(), NotifyItemChangedListener {

    private var pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        job.cancel()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: DailyActivityItemBinding =
            DailyActivityItemBinding.inflate(inflater, parent, false)
        return ActivityViewHolder(itemBinding, pool)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item: ActivityAdapterItem = items[position]
        holder.bind(item, uiScope)
        holder.setNotifyItemChangedListener(this)
    }

    override fun getItemCount(): Int = items.size

    class ActivityDiffCallBack(
        private val newItems: List<ActivityAdapterItem>,
        private val oldItems: MutableList<ActivityAdapterItem>
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

    fun addAll(items: List<ActivityAdapterItem>) {
        val diffCallBack = ActivityDiffCallBack(items, this.items)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.items.addAll(items)

        // calls the adapter's notify methods after diff is calculated
        diffResult.dispatchUpdatesTo(this)
    }

    override fun notifyViewToggle(position: Int) {
        notifyItemChanged(position)
    }

    class ActivityViewHolder(
        private val binding: DailyActivityItemBinding,
        private val pool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val context = binding.root.context
        private val res = context.resources
        private lateinit var item: ActivityAdapterItem
        private lateinit var listener: NotifyItemChangedListener

        init {
            binding.activityView.setOnClickListener(this)
        }

        fun setNotifyItemChangedListener(listener: NotifyItemChangedListener) {
            this.listener = listener
        }

        fun bind(item: ActivityAdapterItem, scope: CoroutineScope) {
            this.item = item
            scope.launch {
                val total = numberOfComments()
                binding.apply {
                    init(adapterPosition, total)
                    setupNestedRecyclerView(scope, total)
                    reconfigure()
                    executePendingBindings()
                }
            }
        }

        /**
         * Sets up the date, progress and total number of comments per [Activity]
         * per day
         * @param pos the position of the item within this Adapter
         * @param activityWithTasks daily activity of the [User]
         */
        private fun DailyActivityItemBinding.init(pos: Int, total: Int) {
            dayText.text = when (pos) {
                0 -> res.getString(R.string.today)
                1 -> res.getString(R.string.yesterday)
                else -> { dayText.visibility = View.GONE; null }
            }

            dateText.text = item.formatDate()

            if (item.tasks.isNotEmpty() && total > 0) {
                emptyActivityView.visibility = View.GONE; activityView.visibility = View.VISIBLE
                ratioCompleted.text =
                    res.getString(
                        R.string.progress_ratio,
                        item.numberOfTasksCompleted(),
                        item.tasks.size)
                commentsText.text =
                    res.getString(R.string.num_comments, total)
            }

        }

        private suspend fun numberOfComments(): Int {
            return withContext(Dispatchers.IO) {
                item.totalNumberOfComments(context)
            }
        }

        /**
         * Creates the nested recycler view for the list of [Task] nested within an [Activity]
         * @param activityWithTasks list of [Task] scheduled per day by the User
         */
        private fun DailyActivityItemBinding.setupNestedRecyclerView(scope: CoroutineScope, total: Int) {
            scope.launch {
                val layoutManager = LinearLayoutManager(
                    tasksCommentsRecyclerView.context,
                    LinearLayoutManager.VERTICAL, false
                )
                layoutManager.initialPrefetchItemCount = total + item.tasks.size

                // Create sub-item view adapter
                val adapter = TaskWithCommentAdapter(flatMap())

                tasksCommentsRecyclerView.adapter = adapter
                tasksCommentsRecyclerView.layoutManager = layoutManager
                tasksCommentsRecyclerView.setRecycledViewPool(pool)
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
        private fun DailyActivityItemBinding.reconfigure() {
            tasksCommentsRecyclerView.visibility = if (item.expanded) {
                panel.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.light_gray_bg_color
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