package com.example.relaxation.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relaxation.R
import com.example.relaxation.databinding.FragmentProfileBinding
import com.example.relaxation.databinding.FragmentSettingsBinding
import com.example.relaxation.ui.activities.RegistrationActivity
import com.example.relaxation.ui.utils.auth
import com.example.relaxation.ui.utils.initFirebase
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initFirebase()
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), RegistrationActivity::class.java))
        }
        return binding.root
    }

}