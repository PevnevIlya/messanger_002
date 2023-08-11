package com.example.relaxation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.example.relaxation.R
import com.example.relaxation.databinding.ActivityRegistrationBinding
import com.example.relaxation.ui.utils.CHILD_EMAIL
import com.example.relaxation.ui.utils.CHILD_ID
import com.example.relaxation.ui.utils.CHILD_NAME
import com.example.relaxation.ui.utils.CHILD_PASSWORD
import com.example.relaxation.ui.utils.NODE_USERS
import com.example.relaxation.ui.utils.REF_DATABASE_ROOT
import com.example.relaxation.ui.utils.UID
import com.example.relaxation.ui.utils.auth
import com.example.relaxation.ui.utils.initFirebase
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        CheckIfUserIsEmailVerified()
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.loginView.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
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
            if (binding.passwordEdit.length() < 8) {
                binding.passwordLayout.helperText = "Make password with 8 or more symbols!"
                return@setOnClickListener
            }
            if (binding.passwordEdit.text.toString() != binding.repeatPasswordEdit.text.toString()) {
                binding.repeatPasswordLayout.helperText = "Repeat your password correctly!"
                return@setOnClickListener
            }
            val email = binding.emailEdit.text.toString().trim()
            val password = binding.passwordEdit.text.toString().trim()
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val dateMap = mutableMapOf<String, Any>()
                    dateMap[CHILD_ID]
                    dateMap[CHILD_EMAIL] = email
                    dateMap[CHILD_PASSWORD] = password
                    dateMap[CHILD_NAME] = UID

                    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).updateChildren(dateMap)
                        .addOnCompleteListener {task ->
                            if (task.isSuccessful) {
                                auth.currentUser?.sendEmailVerification()
                                    ?.addOnSuccessListener {
                                        startActivity(Intent(this, VerificationActivity::class.java))
                                    }
                                    ?.addOnFailureListener {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setTitle("Ошибка")
                                        builder.setMessage("Мы не смогли отправить вам письмо, возможно проблема в нестабильном подключении к сети")
                                        builder.setNeutralButton("Ok") { dialog, which ->

                                        }
                                        builder.show()
                                    }
                            } else {
                                binding.emailLayout.helperText =
                                    "User with this email already exists!"
                            }
                        }
                }
            }
        }
    }

    private fun CheckIfUserIsEmailVerified(){
        if(auth.currentUser?.isEmailVerified == true){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
