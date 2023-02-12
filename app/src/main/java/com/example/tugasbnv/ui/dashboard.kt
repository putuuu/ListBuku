package com.example.tugasbnv.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbnv.MainActivity
import com.example.tugasbnv.R
import com.example.tugasbnv.adapter.RecycleBukuAdapter
import com.example.tugasbnv.model.Buku
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class dashboard : Fragment() {

    private lateinit var rv : RecyclerView
    private lateinit var rvAdapter: RecycleBukuAdapter

    private lateinit var datajudul : String
    private lateinit var datapengarang : String

    private var books = arrayListOf<Buku>()

    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        rv = view.findViewById(R.id.recyclerview)
        db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef = db.collection("users").document(uid)

        usersRef.collection("books").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val judul = document.get("Judul").toString()
                    val pengarang = document.get("Pengarang").toString()
                    books.add(Buku(judul,pengarang))
                }
                rvAdapter = RecycleBukuAdapter(books)
                rv.adapter = rvAdapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        rv.layoutManager = LinearLayoutManager(context)
        return view
    }


}