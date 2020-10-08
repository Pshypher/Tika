package com.example.android.tika.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.R
import com.example.android.tika.data.models.Activity
import com.example.android.tika.databinding.DailyActivityItemBinding
import com.example.android.tika.utils.flatMapTaskObject
import com.example.android.tika.utils.formatDate
import com.example.android.tika.utils.totalNumberOfComments

interface NotifyItemChangedListener {
    fun notifyViewToggle(position: Int)
}

class ActivityAdapter(private val activities: MutableList<Activity>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>(), NotifyItemChangedListener {

    private var pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: DailyActivityItemBinding =
            DailyActivityItemBinding.inflate(inflater, parent, false)
        return ActivityViewHolder(itemBinding, pool)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity: Activity = activities[position]
        holder.bind(activity)
        holder.setNotifyItemChangedListener(this)
    }

    override fun getItemCount(): Int = activities.size

    class ActivityDiffCallBack(private val newItems : List<Activity>, private val oldItems : List<Activity>)
        : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldItems[oldItemPosition]
            val new = newItems[newItemPosition]
            return old == new
        }
    }

    fun addAll(activities: List<Activity>) {
        val diffCallBack = ActivityDiffCallBack(activities, this.activities)
        val diffResult = DiffUtil.calculateDiff(diffCallBack)

        this.activities.addAll(activities)

        // calls the adapter's notify methods after diff is calculated
        diffResult.dispatchUpdatesTo(this)
    }

    override fun notifyViewToggle(position: Int) {
        notifyItemChanged(position)
    }

    class ActivityViewHolder(private val binding: DailyActivityItemBinding,
                             private val pool: RecyclerView.RecycledViewPool)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val context = binding.root.context
        private val res = context.resources
        private lateinit var activity: Activity
        private lateinit var listener: NotifyItemChangedListener

        init {
            binding.activityView.setOnClickListener(this)
        }

        fun setNotifyItemChangedListener(listener: NotifyItemChangedListener) {
            this.listener = listener
        }

        fun bind(activity: Activity) {

            this.activity = activity

            binding.apply {
                init(adapterPosition, activity)
                setupNestedRecyclerView(activity)
                reconfigure(activity)
                executePendingBindings()
            }
        }

        /**
         * Sets up the date, progress and total number of comments per [Activity]
         * per day
         * @param pos the position of the item within this Adapter
         * @param activity daily activity of the [User]
         */
        private fun DailyActivityItemBinding.init(pos: Int, activity: Activity) {
            dayText.text = when (pos) {
                0 -> res.getString(R.string.today)
                1 -> res.getString(R.string.yesterday)
                else -> {
                    binding.dayText.visibility = View.GONE; null
                }
            }

            dateText.text = formatDate(activity.date)

            if (activity.tasks.isNotEmpty() && totalNumberOfComments((activity.tasks)) > 0) {
                emptyActivityView.visibility = View.GONE; activityView.visibility = View.VISIBLE
                ratioCompleted.text =
                    res.getString(R.string.progress_ratio, activity.completed, activity.total)
                commentsText.text =
                    res.getString(R.string.num_comments, totalNumberOfComments(activity.tasks))
            }
        }

        /**
         * Creates the nested recycler view for the list of [Task] nested within an [Activity]
         * @param activity list of [Task] scheduled per day by the User
         */
        private fun DailyActivityItemBinding.setupNestedRecyclerView(activity: Activity) {
            val layoutManager = LinearLayoutManager(tasksCommentsRecyclerView.context,
                LinearLayoutManager.VERTICAL, false)
            layoutManager.initialPrefetchItemCount =
                totalNumberOfComments((activity.tasks)) + activity.tasks.size

            // Create sub-item view adapter
            val adapter = ActivityDetailAdapter(flatMapTaskObject(activity.tasks))

            tasksCommentsRecyclerView.adapter = adapter
            tasksCommentsRecyclerView.layoutManager = layoutManager
            tasksCommentsRecyclerView.setRecycledViewPool(pool)
        }

        /**
         *  Logic used to control the collapsible layout
         * @param activity an [Activity] per day for the [User]
         */
        private fun DailyActivityItemBinding.reconfigure(activity: Activity) {
            tasksCommentsRecyclerView.visibility = if (activity.expanded) {
                panel.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray_bg_color))
                View.VISIBLE }
            else {
                panel.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                View.GONE }
        }

        override fun onClick(v: View?) {
            activity.expanded = !activity.expanded
            listener.notifyViewToggle(adapterPosition)
        }
    }
}