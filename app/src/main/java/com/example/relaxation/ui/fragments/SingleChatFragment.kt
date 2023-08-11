package com.example.relaxation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relaxation.R
import com.example.relaxation.databinding.FragmentSettingsBinding
import com.example.relaxation.databinding.FragmentSingleChatBinding
import com.example.relaxation.ui.utils.initFirebase

class SingleChatFragment : Fragment() {

    private lateinit var binding: FragmentSingleChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSingleChatBinding.inflate(inflater, container, false)
        initFunc()
        return binding.root
    }

    private fun initFunc(){

    }
}