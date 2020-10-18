package com.example.android.tika.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.tika.data.database.Comment
import com.example.android.tika.data.presentation.CommentItem
import com.example.android.tika.databinding.CommentLayoutBinding
import com.example.android.tika.databinding.TaskTitleItemBinding
import com.example.android.tika.utils.getAuthor
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

class TaskWithCommentAdapter(val scope: CoroutineScope?)
    : ListAdapter<Any, RecyclerView.ViewHolder>(TaskCommentDiffCallBack()) {

    companion object {
        const val TITLE = 0
        const val COMMENT = 1
    }

    class TaskCommentDiffCallBack : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(old: Any, new: Any): Boolean {
            var result = false
            if (old::class.simpleName == new::class.simpleName) {
                if (old is Comment && new is Comment) result = old.commentId == new.commentId
                else result = areContentsTheSame(old, new)
            }

            return result
        }

        override fun areContentsTheSame(old: Any, new: Any): Boolean {
            var result: Boolean
            if (old is String && new is String) {
                result = (old as String) == (new as String)
            } else result = (old as Comment) == (new as Comment)

            return result
        }

    }

    class TitleViewHolder(private val itemBinding: TaskTitleItemBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Any) {

            val resources = itemBinding.root.context.resources

            itemBinding.apply {
                titleText.text = (item as String)
                val layoutParams = titleText.layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin  = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, when (adapterPosition) {
                        0 -> 0F
                        else -> 16F
                    }, resources.displayMetrics).toInt()
                titleText.layoutParams = layoutParams
            }
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val attachToParent = false
                val inflater = LayoutInflater.from(parent.context)
                return TitleViewHolder(TaskTitleItemBinding.inflate(inflater, parent, attachToParent))
            }
        }
    }

    class CommentViewHolder private constructor(private val itemBinding: CommentLayoutBinding) :
            RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Any, scope: CoroutineScope?) {
            scope?.launch {
                val entity = item as Comment
                val comment = CommentItem(entity.commentId, entity.message, null)
                comment.author = author(entity)
                itemBinding.comment = comment
            }
        }

        suspend fun author(entity: Comment): String {
            val context = itemBinding.root.context
            return withContext(Dispatchers.IO) {
                getAuthor(context, entity)
            }
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val attachToParent = false
                val inflater = LayoutInflater.from(parent.context)
                return CommentViewHolder(CommentLayoutBinding.inflate(inflater, parent, attachToParent))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TITLE -> TitleViewHolder.from(parent)
            COMMENT -> CommentViewHolder.from(parent)
            else -> throw IllegalArgumentException("Unknown ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val  item = getItem(position)
        when (holder.itemViewType) {
            TITLE -> (holder as TitleViewHolder).bind(item)
            COMMENT -> (holder as CommentViewHolder).bind(item, scope)
            else ->  throw IllegalArgumentException("Unknown ViewType ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is String) TITLE else COMMENT
    }
}