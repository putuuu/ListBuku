package com.example.tugasbnv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbnv.R
import com.example.tugasbnv.model.Buku

class RecycleBukuAdapter(private val items: List<Buku>) :RecyclerView.Adapter<RecycleBukuAdapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val txtjudul : TextView = itemView.findViewById(R.id.txt_judul)
        private val txtpengarang : TextView = itemView.findViewById(R.id.txt_pengarang)
        fun bindItem(buku: Buku) {
            txtjudul.text = buku.Judul
            txtpengarang.text = "karya: "+buku.Pengarang
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, buku.Judul, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    }