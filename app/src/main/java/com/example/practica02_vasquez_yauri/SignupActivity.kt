package com.example.practica02_vasquez_yauri

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practica02_vasquez_yauri.model.UserModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val etFullName: EditText =findViewById(R.id.etFullNameR)
        val etEmail: EditText =findViewById(R.id.etEmailR)
        val etPassword: EditText =findViewById(R.id.etPasswordR)
        val btnSave: Button =findViewById(R.id.btnSave)

        val db = FirebaseFirestore.getInstance()
        val auth= FirebaseAuth.getInstance()


        btnSave.setOnClickListener{
            val fullName=etFullName.text.toString()
            val email=etEmail.text.toString()
            val password=etPassword.text.toString()


            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        val user: FirebaseUser? = auth.currentUser
                        val uid =user?.uid
                        val userModel = UserModel(fullName,email,uid)

                        db.collection("users")
                            .add(userModel)
                            .addOnSuccessListener {
                                Snackbar.make(
                                    findViewById(android.R.id.content)
                                    ,"Usuario registrado"
                                    , Snackbar.LENGTH_LONG
                                ).show()
                                CleanCampos(etFullName,etEmail,etPassword)
                                //redirigir a la actividad SigninActivity
                                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                startActivity(intent)




                            }.addOnFailureListener{error->
                                Snackbar.make(
                                    findViewById(android.R.id.content)
                                    ,"Error al registrar usuario: ${error.message}"
                                    , Snackbar.LENGTH_LONG
                                ).show()
                                CleanCampos(etFullName,etEmail,etPassword)
                            }


                    }
                }
        }

    }
}

fun CleanCampos(etFullName:  EditText, etEmail: EditText, etPassword: EditText){
    etFullName.text.clear()
    etEmail.text.clear()
    etPassword.text.clear()
}