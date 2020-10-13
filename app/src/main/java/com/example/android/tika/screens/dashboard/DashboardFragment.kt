package com.example.android.tika.screens.dashboard

import android.os.Bundle
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

import com.example.android.tika.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var activityAdapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskAdapter = TaskAdapter(mutableListOf())
        activityAdapter = ActivityAdapter(mutableListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val viewModelFactory = DashboardViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)

        viewModel.load()

        // Inflate the layout for this fragment
        val binding: FragmentDashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        viewModel.activities.observe(viewLifecycleOwner, { activities ->
            activityAdapter.swap(activities)
        })

        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            taskAdapter.swap(tasks)
        })

        val taskPanel = binding.taskPanel
        val activityPanel = binding.activityPanel

        taskPanel.adapter = taskAdapter
        activityPanel.adapter = activityAdapter

        return binding.root
    }
}