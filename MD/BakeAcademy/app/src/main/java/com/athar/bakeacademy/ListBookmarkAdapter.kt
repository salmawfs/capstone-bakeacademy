package com.athar.bakeacademy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ListBookmarkAdapter(private val listBookmark: List<BookmarkItem>) : RecyclerView.Adapter<ListBookmarkAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: BookmarkItem)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaRotiBookmark: TextView = itemView.findViewById(R.id.tvNamaRotiBookmark)
        val img: ImageView =  itemView.findViewById(R.id.imvRotiBookmark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listBookmark.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roti = listBookmark[position]
        val photoUrl = "https://storage.googleapis.com/bakeacademy-bucket/upload-foto/roti/" + roti.foto
        holder.tvNamaRotiBookmark.text = roti.namaRoti
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listBookmark[holder.adapterPosition])
        }
        Glide.with(holder.itemView)
            .load(photoUrl)
            .fitCenter()
            .into(holder.img)


    }
}