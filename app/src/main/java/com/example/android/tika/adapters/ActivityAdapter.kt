package com.example.android.tika.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.R
import com.example.android.tika.data.models.Activity
import com.example.android.tika.databinding.DailyActivityItemBinding
import com.example.android.tika.utils.flatMapTaskObject
import com.example.android.tika.utils.formatDate
import com.example.android.tika.utils.totalNumberOfComments

class ActivityAdapter(private val activities: Array<Activity>, private val context: Context) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    private var pool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    class ActivityViewHolder(private val binding: DailyActivityItemBinding, private val res: Resources,
                              private val pool: RecyclerView.RecycledViewPool)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: Activity, position: Int, ) {
            binding.apply {
                if (activity.tasks.isNotEmpty()) {
                    emptyActivityView.visibility = View.GONE
                    activityView.visibility = View.VISIBLE
                    ratioCompleted.text =
                        res.getString(R.string.progress_ratio, activity.completed, activity.total)
                    commentsText.text =
                        res.getString(R.string.num_comments, totalNumberOfComments(activity.tasks))
                }

                dayText.text = when (position) {
                    0 ->  res.getString(R.string.today)
                    1 -> res.getString(R.string.yesterday)
                    else -> { binding.dayText.visibility = View.GONE; null }
                }

                dateText.text = formatDate(activity.date)

                val layoutManager = LinearLayoutManager(
                    tasksCommentsRecyclerView.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                layoutManager.initialPrefetchItemCount =
                    totalNumberOfComments((activity.tasks)) + activity.tasks.size

                // Create sub-item view adapter
                val adapter = TaskAdapter(flatMapTaskObject(activity.tasks))

                tasksCommentsRecyclerView.adapter = adapter
                tasksCommentsRecyclerView.layoutManager = layoutManager
                tasksCommentsRecyclerView.setRecycledViewPool(pool)

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: DailyActivityItemBinding =
            DailyActivityItemBinding.inflate(inflater, parent, false)
        return ActivityViewHolder(itemBinding, context.resources, pool)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity: Activity = activities[position]
        holder.bind(activity, position)
    }

    override fun getItemCount(): Int = activities.size
}