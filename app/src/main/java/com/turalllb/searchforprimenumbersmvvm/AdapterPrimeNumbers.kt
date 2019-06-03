package com.turalllb.searchforprimenumbersmvvm

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AdapterPrimeNumbers(context: Context, private val primeNumbers: List<Long>) :
    RecyclerView.Adapter<AdapterPrimeNumbers.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.rv_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = primeNumbers.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = primeNumbers[position].toString()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text_item)
    }
}