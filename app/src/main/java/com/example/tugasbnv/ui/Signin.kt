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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Signin : AppCompatActivity() {
    lateinit var db : FirebaseFirestore
    val firebaseauth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val btnSignin = findViewById<Button>(R.id.button2)
        val edemail = findViewById<EditText>(R.id.editTextTextEmailAddress2)
        val edpass1 = findViewById<EditText>(R.id.editTextTextPassword2)
        val edpass2 = findViewById<EditText>(R.id.editTextTextPassword3)
        val edusername = findViewById<EditText>(R.id.editTextTextPersonName)

        btnSignin.setOnClickListener{
            val email = checkEmail(edemail)
            val password = checkPassword(edpass1, edpass2)
            val username = checkUsername(edusername)

            addUser(email, password, username)

        }
        val txLogin = findViewById<TextView>(R.id.textView5)
        txLogin.setOnClickListener{
            val intent =  Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun checkEmail(edemail : EditText): String {
        var email = ""
        if (edemail.text.isEmpty()) {
            edemail.error = "e-mail not filled"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edemail.text).matches()) {
            edemail.error = "Incorrect e-mail format"
        } else {
            email = edemail.text.toString()
            edemail.error = null
        }
        return email
    }

    private fun checkPassword(edpass1 : EditText, edpass2 : EditText): String{
        var password = ""
        if (edpass1.text.isEmpty()){
            edpass1.error = "password not filled"
        }else if(edpass1.length() < 8){
            edpass1.error = "password less than 8 characters"
        }
        if (edpass2.text.isEmpty()){
            edpass2.error = "password not filled"
        }else if(edpass2.length() < 8){
            edpass2.error = "password less than 8 characters"
        }
        if (edpass1.length() >= 8 && edpass2.length() >= 8){
            if (edpass1.text.toString() != edpass2.text.toString()){
                edpass1.error = "passwords are not the same"
                edpass2.error = "passwords are not the same"
            }else{
                password = edpass1.text.toString()
                edpass1.error = null
                edpass2.error = null
            }
        }
        return password
    }

    private fun addUser(email : String, password : String, username : String){
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()){
            firebaseauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Success add user")
                        val userId = firebaseauth.currentUser?.uid
                        val db = FirebaseFirestore.getInstance()
                        if (userId != null) {
                            val userData = hashMapOf(
                                "email" to email,
                                "username" to username
                            )
                            db.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Success add user to firestore")
                                }
                                .addOnFailureListener { e ->
                                    Log.w(TAG, "Failed add user to firestore", e)
                                }
                        }
                        val intent =  Intent(this, Login::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "Failed add user", task.exception)
                        Toast.makeText(baseContext, "Email already used.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun checkUsername(edusername : EditText): String{
        var username = edusername.text.toString()
        if (edusername.text.isEmpty()){
            edusername.error = "Username not filled"
        }else{
            val db = FirebaseFirestore.getInstance()
            db.collection("users").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val user = document.get("username")
                        if (user == username){
                            edusername.error = "username has been used"
                            username = ""
                        }
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        }
        return username
    }
}