package com.turalllb.searchforprimenumbersmvvm

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class AdapterPrimeNumbers(context: Context) :
    RecyclerView.Adapter<AdapterPrimeNumbers.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    internal var primeNumbers: List<Int> = arrayListOf()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.rv_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = primeNumbers.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = primeNumbers[position].toString()
    }




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_item)
    }
}