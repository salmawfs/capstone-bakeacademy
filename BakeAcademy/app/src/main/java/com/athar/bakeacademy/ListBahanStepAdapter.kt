package com.athar.bakeacademy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListBahanStepAdapter(private val listBahanStep: List<String>) : RecyclerView.Adapter<ListBahanStepAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvResep: TextView = itemView.findViewById(R.id.tvResep)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_row_bahan_langkah, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = listBahanStep.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resep = listBahanStep[position]
        holder.tvResep.text = resep
    }
}