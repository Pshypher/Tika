package com.example.android.tika.screens.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.example.android.tika.data.database.*
import com.example.android.tika.data.presentation.*


class DashboardViewModel(application: Application)
    : AndroidViewModel(application) {

    private val dataSource = TaskDatabase.getInstance(application).taskDao

    fun getActivities(): LiveData<List<ActivityItem>> {
        return Transformations.map(dataSource.getActivities()) { activities ->
            val items: MutableList<ActivityItem> = mutableListOf()
            activities.forEach {
                items.add(ActivityItem(it.activity.activityId, it.activity.date, it.tasks, false))
            }
            items
        }
    }

    fun getTasks(): LiveData<List<TaskItem>> {
        return Transformations.map(dataSource.getTasks()) { tasks ->
            val items: MutableList<TaskItem> = mutableListOf()
            tasks.forEach {
                items.add(TaskItem(it.taskId, it.title).apply {
                    formatDate(it.startTime.time)
                })
            }
            items
        }

    }

    fun getCoroutineScope() = viewModelScope
}