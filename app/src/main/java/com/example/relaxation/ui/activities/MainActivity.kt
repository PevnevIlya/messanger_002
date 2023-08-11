package com.example.relaxation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.example.relaxation.R
import com.example.relaxation.databinding.ActivityMainBinding
import com.example.relaxation.ui.classes.User
import com.example.relaxation.ui.fragments.ChatsFragment
import com.example.relaxation.ui.fragments.ProfileFragment
import com.example.relaxation.ui.fragments.SettingsFragment
import com.example.relaxation.ui.utils.APP_ACTIVITY
import com.example.relaxation.ui.utils.AppStates
import com.example.relaxation.ui.utils.AppValueEventListener
import com.example.relaxation.ui.utils.CHILD_PHOTO_URL
import com.example.relaxation.ui.utils.FOLDER_PROFILE_IMAGE
import com.example.relaxation.ui.utils.NODE_USERS
import com.example.relaxation.ui.utils.REF_DATABASE_ROOT
import com.example.relaxation.ui.utils.REF_STORAGE_ROOT
import com.example.relaxation.ui.utils.UID
import com.example.relaxation.ui.utils.USER
import com.example.relaxation.ui.utils.initFirebase
import com.example.relaxation.ui.utils.initUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val SettingsFragment = SettingsFragment()
    private val ProfileFragment = ProfileFragment()
    private val ChatsFragment = ChatsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFunc()
        Log.d("Test", "onCreate")
        APP_ACTIVITY = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.Profile -> replaceFragment(ProfileFragment)
                R.id.Messanger -> replaceFragment(ChatsFragment)
                R.id.Settings -> replaceFragment(SettingsFragment)

                else ->{

                }
            }
            true
        }

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initFunc(){
        initFirebase()
        initUser()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit()
    }
}