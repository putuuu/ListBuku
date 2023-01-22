package com.example.tugasbnv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.tugasbnv.R
import com.example.tugasbnv.model.Buku

class BukuAdapter internal constructor(private val context: Context) : BaseAdapter(){

    var books = arrayListOf<Buku>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if(itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        }

        val viewHolder = ViewHolder(itemView as View)

        val hero = getItem(position) as Buku
        viewHolder.bind(hero)
        return itemView
    }

    override fun getItem(position: Int): Any = books[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = books.size

    private inner class ViewHolder internal constructor(view: View) {
        private val txtjudul : TextView = view.findViewById(R.id.txt_judul)
        private val txtpengarang : TextView = view.findViewById(R.id.txt_pengarang)
        internal fun bind(buku: Buku) {
            txtjudul.text = buku.Judul
            txtpengarang.text = buku.Pengarang
        }

    }

}