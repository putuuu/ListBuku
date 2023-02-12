package com.example.tugasbnv.ui

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tugasbnv.MainActivity
import com.example.tugasbnv.R
import com.example.tugasbnv.adapter.RecycleBukuAdapter
import com.example.tugasbnv.model.Buku
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    val firebaseauth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edemail = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val edpass = findViewById<EditText>(R.id.editTextTextPassword)

        val btnlogin = findViewById<Button>(R.id.button)
        btnlogin.setOnClickListener{
//            val email = checkEmail(edemail)
            val email = checkEmail(edemail)
            val password = checkPassword(edpass)

            loginuser(email, password)

        }

        val txSignin = findViewById<TextView>(R.id.textView3)
        txSignin.setOnClickListener{
            val intent =  Intent(this, Signin::class.java)
            startActivity(intent)
        }
    }

    private fun loginuser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()){
            firebaseauth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User login successful")
                        val intent =  Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        db.collection("users").get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    val dbemail = document.get("email").toString()
                                    val username = document.get("username").toString()
                                    if (username == email){
                                        loginuser(dbemail, password)
                                    }
                                }

                            }
                            .addOnFailureListener { exception ->
                                Log.w(TAG, "Error getting documents.", exception)
                            }
                    }else {
                        Log.w(TAG, "User login failed", task.exception)
                        Toast.makeText(baseContext, "Incorrect email or password",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun checkEmail(edemail : EditText): String {
        var email = ""
        if (edemail.text.isEmpty()) {
            edemail.error = "Email or username not filled"
        } else {
            email = edemail.text.toString()
            edemail.error = null
        }
        return email
    }

    private fun checkPassword(edpass1 : EditText): String{
        var password = ""
        if (edpass1.text.isEmpty()){
            edpass1.error = "password not filled"
        }else if(edpass1.length() < 8){
            edpass1.error = "password less than 8 characters"
        }else{
            password = edpass1.text.toString()
            edpass1.error = null
        }
        return password
    }
}