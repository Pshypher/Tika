package com.example.android.tika.screens.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.example.android.tika.data.database.*
import com.example.android.tika.data.presentation.ActivityAdapterItem
import com.example.android.tika.data.presentation.TaskAdapterItem
import com.example.android.tika.data.presentation.formatDate


class DashboardViewModel(application: Application)
    : AndroidViewModel(application) {

    private val dataSource = TaskDatabase.getInstance(application).taskDao

    fun getActivities(): LiveData<List<ActivityAdapterItem>> {
        return Transformations.map(dataSource.getActivities()) { activities ->
            val items: MutableList<ActivityAdapterItem> = mutableListOf()
            activities.forEach {
                items.add(ActivityAdapterItem(it.activity.activityId, it.activity.date, it.tasks, false))
            }
            items
        }
    }

    fun getTasks(): LiveData<List<TaskAdapterItem>> {
        return Transformations.map(dataSource.getTasks()) { tasks ->
            val items: MutableList<TaskAdapterItem> = mutableListOf()
            tasks.forEach {
                items.add(TaskAdapterItem(it.taskId, it.title).apply {
                    formatDate(it.startTime.time)
                })
            }
            items
        }

    }

//    init {
        // fetch()
//        viewModelScope.launch {
//            _tasks.value = dataSource.getTasks()
//            val tasks = Transformations.map(_tasks) {
//                val items = mutableListOf<TaskAdapterItem>()
//                it.forEach { task ->
//                    items.add(TaskAdapterItem(task.taskId, task.title).apply {
//                        formatDate(task.startTime.time)
//                    })
//                }
//                items
//            }
//            _activitiesWithTasks.value = dataSource.getActivities()
//            activities = Transformations.map(_activitiesWithTasks) {
//                val items: MutableList<ActivityAdapterItem> = mutableListOf()
//
//                it?.forEach {
//                    items.add(ActivityAdapterItem(it.activity.activityId, it.activity.date, it.tasks))
//                }
//                items
//            }
//        }
 //   }
}