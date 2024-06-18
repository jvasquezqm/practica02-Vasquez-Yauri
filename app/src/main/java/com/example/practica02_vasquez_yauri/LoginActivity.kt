package com.example.practica02_vasquez_yauri

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignup: Button = findViewById(R.id.btnSignup)
        val auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()

            //Validar si esta vacio los campos de email y password
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(
                    this, "Los campos email y contraseña no pueden estar vacíos",
                    Toast.LENGTH_SHORT
                ).show();
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Bienvenido",
                                Snackbar.LENGTH_LONG
                            ).show()
                            val intent = Intent(this, PrincipalActivity::class.java)
                            startActivity(intent)
                        } else {
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Credenciales inválidas",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

}