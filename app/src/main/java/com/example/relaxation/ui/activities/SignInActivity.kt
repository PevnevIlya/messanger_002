package com.example.relaxation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.example.relaxation.R
import com.example.relaxation.databinding.ActivitySignInBinding
import com.example.relaxation.ui.utils.auth
import com.example.relaxation.ui.utils.initFirebase
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        initFirebase()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        binding.backView.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        binding.confirmButton.setOnClickListener {
            if (TextUtils.isEmpty(binding.emailEdit.text.toString())) {
                binding.emailLayout.helperText = "Enter your email!"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.passwordEdit.text.toString())) {
                binding.passwordLayout.helperText = "Enter your password!"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(binding.repeatPasswordEdit.text.toString())) {
                binding.repeatPasswordLayout.helperText = "Repeat your password!"
                return@setOnClickListener
            }
            if (binding.passwordEdit.text.toString() != binding.repeatPasswordEdit.text.toString()) {
                binding.repeatPasswordLayout.helperText = "Repeat your password correctly!"
                return@setOnClickListener
            }
            var email = binding.emailEdit.text.toString().trim()
            val password = binding.emailEdit.text.toString().trim()
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
                .addOnFailureListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Ошибка")
                builder.setMessage("Неправильные данные.")
                builder.setNeutralButton("Ok") { dialog, which ->
                }
                builder.show()
            }
        }
    }
}