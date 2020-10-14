package com.example.android.tika.screens.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.tika.R
import com.example.android.tika.adapters.ActivityAdapter
import com.example.android.tika.adapters.TaskAdapter
import com.example.android.tika.data.database.ActivityWithTasks

import com.example.android.tika.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private val TAG = "DashboardFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentDashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val activityAdapter = ActivityAdapter(mutableListOf())
        binding.activityPanel.adapter = activityAdapter
        val taskAdapter = TaskAdapter(mutableListOf())
        binding.taskPanel.adapter = taskAdapter

        val application = requireNotNull(this.activity).application
        val viewModelFactory = DashboardViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)

        viewModel.getActivities().observe(viewLifecycleOwner, Observer { activities ->
            Log.d(TAG, activities.toString())
            activityAdapter.swap(activities)
        })

        viewModel.getTasks().observe(viewLifecycleOwner, Observer { tasks ->
            Log.d(TAG, tasks.toString())
            taskAdapter.swap(tasks)
        })

        return binding.root
    }
}