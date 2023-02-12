package com.example.tugasbnv.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import com.bumptech.glide.Glide
import com.example.tugasbnv.R
import com.example.tugasbnv.model.Akun
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class account : Fragment() {

    private lateinit var profilpicture: ImageView
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser

    var akun = Akun("", "", "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val tvusername = view.findViewById<TextView>(R.id.textView6)
        val tvemail = view.findViewById<TextView>(R.id.textView13)
        val tvBuku = view.findViewById<TextView>(R.id.textView15)
        val updateUsername = view.findViewById<ImageView>(R.id.imageView11)
        profilpicture = view.findViewById(R.id.imageView2)
        val logOut = view.findViewById<Button>(R.id.button3)

        if (firebaseUser != null) {
            db.collection("users").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == firebaseUser.uid) {
                            val username = document.get("username").toString()
                            val email = document.get("email").toString()
                            val url = document.get("url").toString()

                            val ref = db.collection("users").document(firebaseUser.uid)
                                .collection("books")
                            ref.get().addOnSuccessListener { result ->
                                val count = result.size()
                                tvBuku.text = count.toString()
                            }

                            akun = Akun(username, email, url)
                            tvemail.text = akun.email
                            tvusername.text = akun.username
                        }
                    }
                }
        }

        logOut.setOnClickListener{
            firebaseAuth.signOut()
            val intent = Intent(context, Login::class.java)
            startActivity(intent)
        }

        updateUsername.setOnClickListener{
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.activity_updateusername, null)

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true

            val popupWindow = PopupWindow(view, width, height, focusable)

            val edusername = view.findViewById<EditText>(R.id.etxt_username)
            val updateusername = view.findViewById<Button>(R.id.btnUpdate1)

            edusername.setText(tvusername.text)

            updateusername.setOnClickListener{
                if (edusername.text.isEmpty()){
                    edusername.setError("Username not filled")
                }else {
                    if (firebaseUser != null){
                        val docref = db.collection("users").document(firebaseUser.uid)
                        docref.update("username",edusername.text.toString())
                            .addOnSuccessListener { Log.d("TAG", "Document field has been updated") }
                            .addOnFailureListener { e -> Log.w("TAG", "Error updating document field", e) }
                    }
                    val snackbar = Snackbar.make(view,"username has been updated", Snackbar.LENGTH_LONG)

                    snackbar.show()

                    Snackbar.make(view, "This is a Snackbar", Snackbar.LENGTH_LONG)
                }
            }

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        }

        profilpicture.setOnClickListener {
            Toast.makeText(profilpicture.context, akun.username, Toast.LENGTH_SHORT).show()
        }

        return view

    }

}


