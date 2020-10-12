package com.example.android.tika.screens.dashboard

import android.app.Application
import androidx.lifecycle.*
import com.example.android.tika.data.database.*
import com.example.android.tika.data.presentation.ActivityAdapterItem
import com.example.android.tika.data.presentation.TaskAdapterItem
import com.example.android.tika.data.presentation.formatDate
import kotlinx.coroutines.*

class DashboardViewModel(val dataSource: TaskDao, application: Application)
    : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _owner = MutableLiveData<String>()
    val owner: LiveData<String>
        get() = _owner

    private val _activitiesWithTasks = MutableLiveData<List<ActivityWithTasks>>()
    val activities = Transformations.map(_activitiesWithTasks) {
        val items: MutableList<ActivityAdapterItem> = mutableListOf()
        it.forEach {
            items.add(ActivityAdapterItem(it.activity.activityId, it.activity.date, it.tasks))
        }
        items
    }


    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<TaskAdapterItem>> = Transformations.map(_tasks) {
        val items: MutableList<TaskAdapterItem> = mutableListOf()
        it.forEach {
            items.add(TaskAdapterItem(it.taskId, it.title).apply {
                formatDate(it.startTime.time)
            })
        }
        items
    }

    fun load() {
        uiScope.launch {
            _tasks.value = getTasks()
            _activitiesWithTasks.value = getActivities()
        }
    }

    suspend fun getTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            dataSource.getTasks()
        }
    }

    suspend fun getActivities(): List<ActivityWithTasks> {
        return withContext(Dispatchers.IO) {
            dataSource.getActivities()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}