package com.example.android.tika.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.tika.R
import com.example.android.tika.data.models.*
import com.example.android.tika.databinding.FragmentDashboardBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    private lateinit var activities: List<Activity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        val users = listOf(
            User(0, "Mika","Ghuanzhou"),
            User(1, "Temiloluwa", "Afolabi"),
            User(2, "Tunji", "Ojekunle"),
            User(3, "Saadiq", "Abdullahi"))

        val comments = listOf(
            Comment(users[0], "Well Done!"),
            Comment(users[1], "go Jimi go"),
            Comment(users[2], "Did you close the last comment tag?"),
            Comment(users[3], "I just finished chapter 12 myself, well done"))

        val today: List<Task> = listOf()
        val yesterday: List<Task> = listOf(
            Task("Practice Karate", "", Date(1601451900000),
                Date(1601470500000), users.subList(1, 3), comments.subList(1, 3)),
            Task("Feng-Shui meditation", "", Date(1601476200000),
                Date(1601485200000), users.subList(0, 1), comments.subList(0, 1))
        )
        val other: List<Task> = listOf(
            Task("Complete HTML $ CSS in 30 days", "", Date(1601539200000),
                Date(1601542800000), users.subList(0, 3), comments.subList(0, 3)),
            Task("Reading 48 Laws of Power", "", Date(1601550000000),
                Date(1601553600000), users.subList(3, users.size), comments.subList(3, comments.size)),
            Task("Practice Taekwando Poomse", "I want to get stronger practicing my \n" +
                    "taekwando poomse.", Date(1601493300000), Date(1601497800000),
                users.subList(0, 1), comments.subList(0, 1)),
            Task("Jog for miles", "Trying to get my fitness levels through the roof",
                Date(1601498700000), Date(1601505225000))
        )

        activities = listOf(
            Activity(0, 0, Date(1601593200000), today),
            Activity(1, 2, Date(1601506800000), yesterday),
            Activity(3, 4, Date(1601420400000), other)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentDashboardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        val taskPanel = binding.taskPanel
        val activityPanel = binding.activityPanel

        return null
    }
}