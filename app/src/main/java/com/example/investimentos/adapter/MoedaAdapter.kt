package com.example.investimentos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.R
import com.example.investimentos.model.MoedaModel

class MoedaAdapter : RecyclerView.Adapter<MoedaViewHolder>() {

    private val listaDeMoedas = mutableListOf<MoedaModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoedaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moeda, parent, false)
        return MoedaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoedaViewHolder, position: Int) {
        holder.preencher(listaDeMoedas[position])
    }

    override fun getItemCount(): Int = listaDeMoedas.size

}