package com.example.relaxation.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.relaxation.R
import com.example.relaxation.databinding.ActivityChangeInfoBinding
import com.example.relaxation.databinding.ActivityMainBinding
import com.example.relaxation.databinding.FragmentProfileBinding
import com.example.relaxation.ui.fragments.ProfileFragment
import com.example.relaxation.ui.utils.APP_ACTIVITY
import com.example.relaxation.ui.utils.AppValueEventListener
import com.example.relaxation.ui.utils.CHILD_BIO
import com.example.relaxation.ui.utils.CHILD_NAME
import com.example.relaxation.ui.utils.CHILD_PHOTO_URL
import com.example.relaxation.ui.utils.FOLDER_PROFILE_IMAGE
import com.example.relaxation.ui.utils.NODE_USERNAMES
import com.example.relaxation.ui.utils.NODE_USERS
import com.example.relaxation.ui.utils.REF_DATABASE_ROOT
import com.example.relaxation.ui.utils.REF_STORAGE_ROOT
import com.example.relaxation.ui.utils.UID
import com.example.relaxation.ui.utils.USER
import com.example.relaxation.ui.utils.initFirebase
import com.example.relaxation.ui.utils.initUser
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.util.Locale

class ChangeInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        initFunc()
        Log.d("Test",USER.name)
        binding.profileImage.setOnClickListener{
            CropImage.activity()
                .setAspectRatio(1,1)
                .setRequestedSize(600,600)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this)
        }
        binding.confirmProfileButton.setOnClickListener {
            val NewUsername = binding.nameEdit.text.toString()
            if(NewUsername.isEmpty()){
                binding.nameLayout.helperText = "Поле пустое!"
                return@setOnClickListener
            }
            if(NewUsername == USER.name){
                binding.nameLayout.helperText = "Измените имя!"
                return@setOnClickListener
            }
            REF_DATABASE_ROOT.child(NODE_USERNAMES).addListenerForSingleValueEvent(AppValueEventListener{
                if(it.hasChild(NewUsername)){
                    binding.nameLayout.helperText = "Такое имя уже существует!"
                }else{
                    Log.d("Test", "Next")
                    REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.name).removeValue()
                        .addOnCompleteListener {task1 ->
                            if(task1.isSuccessful){
                                REF_DATABASE_ROOT.child(NODE_USERNAMES).child(NewUsername).setValue(UID)
                                REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_NAME).setValue(binding.nameEdit.text.toString())
                                    .addOnCompleteListener {task2 ->
                                        if(task2.isSuccessful){
                                            USER.name = binding.nameEdit.text.toString()
                                        }
                                    }
                            }
                        }
                    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(binding.bioEdit.text.toString())
                    startActivity(Intent(this, MainActivity::class.java))
                }
            })
        }
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Test", "onActivityResult")
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == AppCompatActivity.RESULT_OK && data != null){
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(UID)
            path.putFile(uri)
                .addOnCompleteListener{task1 ->
                    if (task1.isSuccessful){
                        Log.d("Test", "Profile photo was uploaded")
                        path.downloadUrl.addOnCompleteListener{task2 ->
                            if(task2.isSuccessful){
                                val photoUrl = task2.result.toString()
                                REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
                                    .child(CHILD_PHOTO_URL).setValue(photoUrl)
                                    .addOnCompleteListener { task3 ->
                                        if(task3.isSuccessful){
                                            Log.d("Test", "Photo updated")
                                            Picasso.get().load(photoUrl).placeholder(R.drawable.photo).into(binding.profileImage)
                                            USER.photoUrl = photoUrl
                                        }
                                    }
                            }
                        }
                    }else{
                        Log.d("Test", "Profile photo was not uploaded")
                    }
                }
        }
    }

    private fun initFunc(){
        initFirebase()
        binding.nameEdit.setText(USER.name)
        binding.bioEdit.setText(USER.bio)
        Picasso.get().load(USER.photoUrl).into(binding.profileImage)
    }

}
