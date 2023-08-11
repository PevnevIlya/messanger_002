package com.example.relaxation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.relaxation.databinding.ActivityVerificationBinding
import com.example.relaxation.ui.utils.CHILD_EMAIL
import com.example.relaxation.ui.utils.CHILD_ID
import com.example.relaxation.ui.utils.CHILD_PASSWORD
import com.example.relaxation.ui.utils.auth
import com.example.relaxation.ui.utils.initFirebase
import com.google.firebase.auth.FirebaseAuth

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        initFirebase()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.buttonCheck.setOnClickListener {
            val user = auth.currentUser
            user?.reload()?.addOnCompleteListener {
                if (user.isEmailVerified) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Ошибка")
                    builder.setMessage("Вы не верифицировали введенный email!")
                    builder.setNeutralButton("Ok") { dialog, which ->
                    }
                    builder.show()
                }
            }
        }

        binding.buttonBack.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}