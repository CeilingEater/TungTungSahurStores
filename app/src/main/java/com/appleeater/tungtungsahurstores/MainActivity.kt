package com.appleeater.tungtungsahurstores

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import java.security.Signer

class MainActivity : AppCompatActivity() {

    lateinit var etEmail: EditText

    lateinit var etConfPass: EditText

    private lateinit var etPass: EditText

    private lateinit var btnSignUp: Button

    lateinit var tvRedirection: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cardView = findViewById<MaterialCardView>(R.id.cardContainer)
        val fadeIn = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade_in)
        cardView.startAnimation(fadeIn)

        etEmail = findViewById(R.id.etSEmailAddress)
        etConfPass = findViewById(R.id.etSConfPassword)
        etPass = findViewById(R.id.etSPassword)
        btnSignUp = findViewById(R.id.btnSigned)
        tvRedirection = findViewById(R.id.tvRedirectLogin)

        auth = FirebaseAuth.getInstance()

        val clickAnim =
            android.view.animation.AnimationUtils.loadAnimation(this, R.anim.button_click)
        btnSignUp.setOnClickListener {
            it.startAnimation(clickAnim)
            signUpUser()
        }

        tvRedirection.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

        private fun signUpUser()
        {
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()
            val confirmPassword = etConfPass.text.toString()

            if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank())
            {
                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
                return
            }

            if (pass != confirmPassword)
            {
                Toast.makeText(this, "Password and Confirm password do not match", Toast.LENGTH_SHORT).show()
                return
            }

            val intent = Intent(this, LoginActivity::class.java)
            auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this) {
                if(it.isSuccessful){
                    Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
