package com.example.android.tika.screens.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.tika.data.database.TaskDatabase
import java.lang.IllegalArgumentException

class DashboardViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}