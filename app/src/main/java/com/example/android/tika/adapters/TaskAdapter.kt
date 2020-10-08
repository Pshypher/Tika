package com.example.android.tika.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.data.models.Comment
import com.example.android.tika.data.models.Task
import com.example.android.tika.databinding.CommentLayoutBinding
import com.example.android.tika.databinding.TaskTitleItemBinding
import java.lang.IllegalArgumentException

class TaskAdapter(private val items: List<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TASK = 0
        const val COMMENT = 1
    }

    class TitleViewHolder(private val itemBinding: TaskTitleItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Any) {

            val resources = itemBinding.root.context.resources

            itemBinding.apply {
                titleText.text = (item as Task).title
                if (adapterPosition == 0) {
                    val layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                    layoutParams.topMargin = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics).toInt()
                    layoutParams.leftMargin = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 8F, resources.displayMetrics).toInt()
                    titleText.layoutParams = layoutParams
                }
            }
        }
    }

    class CommentViewHolder(private val itemBinding: CommentLayoutBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Any) {
            val comment = item as Comment
            itemBinding.apply {
                authorText.text = comment.author.firstName
                commentText.text = comment.message
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TASK -> TitleViewHolder(TaskTitleItemBinding.inflate(inflater, parent, false))
            COMMENT -> CommentViewHolder(CommentLayoutBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown ViewType >>> $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TASK -> (holder as TitleViewHolder).bind(items[position])
            COMMENT -> (holder as CommentViewHolder).bind(items[position])
            else ->  throw IllegalArgumentException("Unknown ViewType >>> ${ holder.itemViewType }")
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Task) TASK else COMMENT
    }
}