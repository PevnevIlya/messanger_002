package com.example.relaxation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.relaxation.R
import com.example.relaxation.databinding.FragmentProfileBinding
import com.example.relaxation.databinding.FragmentSettingsBinding
import com.example.relaxation.ui.activities.ChangeInfoActivity
import com.example.relaxation.ui.activities.SearchPersonActivity
import com.example.relaxation.ui.classes.User
import com.example.relaxation.ui.utils.CHILD_BIO
import com.example.relaxation.ui.utils.CHILD_NAME
import com.example.relaxation.ui.utils.CHILD_PHOTO_URL
import com.example.relaxation.ui.utils.FOLDER_PROFILE_IMAGE
import com.example.relaxation.ui.utils.NODE_USERS
import com.example.relaxation.ui.utils.REF_DATABASE_ROOT
import com.example.relaxation.ui.utils.REF_STORAGE_ROOT
import com.example.relaxation.ui.utils.UID
import com.example.relaxation.ui.utils.USER
import com.example.relaxation.ui.utils.auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        initFunc()
        binding.changeButton.setOnClickListener {
            startActivity(Intent(requireActivity(), ChangeInfoActivity::class.java))
        }
        binding.addButton.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchPersonActivity::class.java))
        }
        return binding.root
    }

    private fun initFunc(){
        binding.nameTv.text = USER.name
        binding.bioTv.text = USER.bio
        binding.stateTv.text = USER.state
        Picasso.get().load(USER.photoUrl).into(binding.profileImage)
    }
}