package com.example.tugasbnv.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tugasbnv.R
import com.example.tugasbnv.model.Akun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class account : Fragment() {

    private lateinit var profilpicture: ImageView
    private lateinit var tvusername: TextView
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser
    val firebaseStorage = FirebaseStorage.getInstance()

    var akun = Akun("", "", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        tvusername = view.findViewById(R.id.textView6)
        profilpicture = view.findViewById(R.id.imageView2)

        if (firebaseUser != null) {
            db.collection("users").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == firebaseUser.uid) {
                            val username = document.get("username").toString()
                            val email = document.get("email").toString()
                            val url = document.get("url").toString()
                            akun = Akun(username, email, url)
                            tvusername.text = akun.username
                        }
                    }
                }
        }

        profilpicture.setOnClickListener {
            Toast.makeText(profilpicture.context, akun.username, Toast.LENGTH_SHORT).show()
        }

        return view
    }
}


