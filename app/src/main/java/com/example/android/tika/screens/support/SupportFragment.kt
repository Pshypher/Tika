package com.example.android.tika.screens.support

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.android.tika.R

class SupportFragment : Fragment() {

    companion object {
        fun newInstance() = SupportFragment()
    }

    private lateinit var viewModel: SupportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.support_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SupportViewModel::class.java)
        // TODO: Use the ViewModel
    }

}