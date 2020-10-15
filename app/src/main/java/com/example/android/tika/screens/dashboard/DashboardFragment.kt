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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding: FragmentDashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = DashboardViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)

        val activityAdapter = ActivityAdapter()
        binding.activityPanel.adapter = activityAdapter

        val taskAdapter = TaskAdapter()
        binding.taskPanel.adapter = taskAdapter

        activityAdapter.scope = viewModel.getCoroutineScope()
        viewModel.getActivities().observe(viewLifecycleOwner, Observer { activities ->
            activities?.let {
                activityAdapter.items = it
            }
        })

        viewModel.getTasks().observe(viewLifecycleOwner, Observer { tasks ->
            tasks?.let {
                taskAdapter.tasks = it
            }
        })

        return binding.root
    }
}