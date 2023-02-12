package com.example.tugasbnv.adapter

import android.content.ContentValues
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbnv.MainActivity
import com.example.tugasbnv.R
import com.example.tugasbnv.model.Buku
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val uid = FirebaseAuth.getInstance().currentUser!!.uid
val db = FirebaseFirestore.getInstance()

class RecycleBukuAdapter(private val items: List<Buku>) :RecyclerView.Adapter<RecycleBukuAdapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val txtjudul : TextView = itemView.findViewById(R.id.txt_judul)
        private val txtpengarang : TextView = itemView.findViewById(R.id.txt_pengarang)

        fun bindItem(buku: Buku) {
            txtjudul.text = buku.Judul
            txtpengarang.text = "Pengarang: "+buku.Pengarang
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
        holder.itemView.setOnClickListener{

            val item = items[position]

            val inflater = LayoutInflater.from(holder.itemView.context)
            val popupView = inflater.inflate(R.layout.activity_updatedeletepopup, null)

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT

            val focusable = true
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            val update = popupView.findViewById<Button>(R.id.btnUpdate)
            val delete = popupView.findViewById<Button>(R.id.btnDelete)
            val judul = popupView.findViewById<EditText>(R.id.etxt_judul2)
            val pengarang = popupView.findViewById<EditText>(R.id.etxt_pengarang2)

            judul.setText(item.Judul)
            pengarang.setText(item.Pengarang)

            update.setOnClickListener{
                if(judul.text.isEmpty()){
                    judul.setError("the title of the book is still empty")
                }
                if(pengarang.text.isEmpty()){
                    pengarang.setError("the author of the book is still vacant")
                }
                if(judul.text.isNotEmpty() && pengarang.text.isNotEmpty()){
                    updateBuku(item.Judul, judul.text.toString(), pengarang.text.toString())
                    val mySnackbar: Snackbar = Snackbar.make(holder.itemView,
                        "Data has been updated ", 1000)
                    mySnackbar.show()
                    popupWindow.dismiss()
                }
            }

            delete.setOnClickListener{
                deleteBuku(item.Judul)
                val mySnackbar: Snackbar = Snackbar.make(holder.itemView,
                    "Data has been deleted ", 1000)
                mySnackbar.show()
                popupWindow.dismiss()
            }

            popupWindow.showAtLocation(holder.itemView, Gravity.CENTER, 0, -150)
        }
    }

    private fun deleteBuku(judul: String) {
        db.collection("users").document(uid).collection("books").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.get("Judul") == judul){
                        val iddocument = document.id
                        db.collection("users").document(uid)
                            .collection("books").document(iddocument).delete(

                            ).addOnSuccessListener {
                                Log.d(ContentValues.TAG, "Data Terdelete")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    fun updateBuku(judul : String, edJudul : String, ePenulis : String) {
        db.collection("users").document(uid).collection("books").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.get("Judul") == judul){
                        val iddocument = document.id
                        db.collection("users").document(uid)
                            .collection("books").document(iddocument).update(
                                "Judul",edJudul,"Pengarang",ePenulis
                            ).addOnSuccessListener {
                                Log.d(ContentValues.TAG, "Data Terupdate")
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
        notifyDataSetChanged()
    }
}