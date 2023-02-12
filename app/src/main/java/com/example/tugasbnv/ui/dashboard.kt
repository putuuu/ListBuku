package com.example.tugasbnv.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbnv.R
import com.example.tugasbnv.adapter.RecycleBukuAdapter
import com.example.tugasbnv.model.Buku
import com.google.android.material.snackbar.Snackbar.make
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

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
        val search = view.findViewById<SearchView>(R.id.searchView)

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

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                filterList(s)
                return true
            }
        })

        rv.layoutManager = LinearLayoutManager(context)
        return view
    }

    private fun filterList(s: String) {
        var filterList = arrayListOf<Buku>()
        if (s.isEmpty()) {
            filterList = books
        } else {
            for (buku in books) {
                if (buku.Judul.lowercase().contains(s.lowercase(Locale.getDefault()))) {
                    filterList.add(buku)
                }
                if (buku.Pengarang.lowercase().contains(s.lowercase(Locale.getDefault()))){
                    filterList.add(buku)
                }
            }
        }
        if (filterList.isEmpty()) {
            Toast.makeText(context, "book not found", Toast.LENGTH_SHORT).show()
        } else {
            rv.adapter = RecycleBukuAdapter(filterList)
        }

    }


}