package com.example.tugasbnv.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.tugasbnv.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class add : Fragment() {

    private lateinit var eJudul : EditText
    private lateinit var ePengarang : EditText
    private lateinit var tambah : Button

    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add, container, false)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()

        tambah = view.findViewById(R.id.btnTambah)
        eJudul = view.findViewById(R.id.etxt_judul)
        ePengarang = view.findViewById(R.id.etxt_pengarang)
        eJudul.requestFocus()
        tambah.setOnClickListener{

            val judul = checkJudul(eJudul)
            val pengarang = checkPengarang(ePengarang)

            if (judul.isNotEmpty() && pengarang.isNotEmpty()){
                val data = hashMapOf(
                    "Judul" to judul,
                    "Pengarang" to pengarang
                )
                db.collection("users").document(uid)
                    .collection("books").add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firestore",
                            "Data berhasil ditambahkan dengan ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error menambahkan data", e)
                    }
                eJudul.setText("")
                ePengarang.setText("")
                eJudul.requestFocus()
                val mySnackbar: Snackbar = Snackbar.make(view, "Book added", 1000)
                mySnackbar.show()
            }

        }
        return view
    }

    fun checkJudul(ejudul : EditText): String {
        var judul = ""
        if (eJudul.length()==0){
            eJudul.setError("Title cannot be empty")
        }else{
          judul = eJudul.text.toString()
        }
        return judul
    }

    fun checkPengarang(ePengarang : EditText): String{
        var pengarang = ""
        if (ePengarang.length()==0){
            ePengarang.setError("Author cannot be empty")
        }else{
            pengarang = ePengarang.text.toString()
        }
        return pengarang
    }

}