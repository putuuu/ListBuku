package com.example.tugasbnv.ui

import android.app.Activity.RESULT_OK
import android.app.DownloadManager.Request
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.net.URL

class account : Fragment() {

    companion object {
        const val REQUEST_CAMERA = 100
    }

    private lateinit var profilpicture: ImageView
    val db = FirebaseFirestore.getInstance()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser


    var akun = Akun("", "")
    private lateinit var imageurl : Uri

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
//            if (firebaseUser.photoUrl != null){
//                Picasso.get().load(firebaseUser.photoUrl).into(profilpicture)
//            }else{
//                Picasso.get().load("https://picsum.photos/seed/picsum/200/300").into(profilpicture)
//            }

            db.collection("users").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id == firebaseUser.uid) {
                            val username = document.get("username").toString()
                            val email = document.get("email").toString()

                            val ref = db.collection("users").document(firebaseUser.uid)
                                .collection("books")
                            ref.get().addOnSuccessListener { result ->
                                val count = result.size()
                                tvBuku.text = count.toString()
                            }

                            akun = Akun(username, email)
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

//        profilpicture.setOnClickListener {
//            intentCamera()
//        }

        return view

    }

//    private fun intentCamera() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {intent ->
//            activity?.packageManager?.let {
//                intent.resolveActivity(it).also {
//                    startActivityForResult(intent, REQUEST_CAMERA)
//                }
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
//            val imgBitmap = data?.extras?.get("data") as Bitmap
//            uploadImage(imgBitmap)
//        }
//    }
//
//    private fun uploadImage(imgBitmap: Bitmap) {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        val ref = FirebaseStorage.getInstance().reference
//            .child("img/${FirebaseAuth.getInstance().currentUser?.uid}")
//
//        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//        val image : ByteArray = byteArrayOutputStream.toByteArray()
//
//        ref.putBytes(image)
//            .addOnCompleteListener{
//                if(it.isComplete){
//                    ref.downloadUrl.addOnCompleteListener {
//                        it.result?.let{
//                            imageurl = it
//                            profilpicture.setImageBitmap(imgBitmap)
//                            val image : Uri = when{
//                                ::imageurl.isInitialized -> imageurl
//                                firebaseUser?.photoUrl == null ->
//                                    Uri.parse("https://picsum.photos/seed/picsum/200/300")
//                                else -> firebaseUser.photoUrl!!
//                            }
//                            UserProfileChangeRequest.Builder().setPhotoUri(image).build()
//                        }
//                    }
//                }
//            }
//    }

}


