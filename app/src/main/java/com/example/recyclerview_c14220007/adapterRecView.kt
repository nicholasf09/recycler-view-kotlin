package com.example.recyclerview_c14220007

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.widget.Toast
import java.lang.reflect.Array

class adapterRecView (private val listWayang: ArrayList<wayang>) : RecyclerView
.Adapter<adapterRecView.ListViewHolder>(){
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _namaWayang = itemView.findViewById<TextView>(R.id.namaWayang)
        var _karakterWayang = itemView.findViewById<TextView>(R.id.karakterWayang)
        var _deskripsiWayang = itemView.findViewById<TextView>(R.id.deskripsiWayang)
        var _gambarWayang = itemView.findViewById<ImageView>(R.id.gambarWayang)
    }
    private lateinit var onItemClickCallBack: onItemClickCallback

    interface onItemClickCallback {
        fun onItemClicked(data: wayang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listWayang.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var wayang = listWayang[position]
        holder._namaWayang.setText(wayang.nama)
        holder._karakterWayang.setText(wayang.karakter)
        holder._deskripsiWayang.setText(wayang.deskripsi)
        Log.d("gambar", wayang.foto)
        Picasso.get().load(wayang.foto).into(holder._gambarWayang)

        holder._gambarWayang.setOnClickListener {
            onItemClickCallBack.onItemClicked(listWayang[position])
        }
    }
    fun setOnItemClickCallback(onItemClickCallback: onItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }
}