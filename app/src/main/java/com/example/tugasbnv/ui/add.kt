package com.example.tugasbnv.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tugasbnv.R
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
        val db = FirebaseFirestore.getInstance()

        tambah = view.findViewById(R.id.btnTambah)
        eJudul = view.findViewById(R.id.etxt_judul)
        ePengarang = view.findViewById(R.id.etxt_pengarang)
        eJudul.requestFocus()
        tambah.setOnClickListener{
            var i = 3
            if (eJudul.text.toString() == ""){
                eJudul.setError("Judul tidak boleh kosong")
                i -= 1
            }
            if (ePengarang.text.toString() == ""){
                ePengarang.setError("Pengarang tidak boleh kosong")
                i -= 2
            }

            if (i == 3){
                val judul = eJudul.text.toString()
                val pengarang = ePengarang.text.toString()
                val data = hashMapOf(
                    "Judul" to judul,
                    "Pengarang" to pengarang

                )
                db.collection("Buku").add(data)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firestore", "Data berhasil ditambahkan dengan ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error menambahkan data", e)
                    }
                eJudul.setText("")
                ePengarang.setText("")
                eJudul.requestFocus()
                Toast.makeText(tambah.context, "Buku ditambahkan", Toast.LENGTH_SHORT).show()
            }else if(i == 0||i ==2){
                eJudul.requestFocus()
            }else{
                ePengarang.requestFocus()
            }
//
        }
        return view
    }

}