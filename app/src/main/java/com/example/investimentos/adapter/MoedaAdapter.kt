package com.example.investimentos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.databinding.ItemMoedaBinding
import com.example.investimentos.model.MoedaModel

class MoedaAdapter(
    var onClick: (moedaModel: MoedaModel) -> Unit = {}
) : RecyclerView.Adapter<MoedaViewHolder>() {

    private val listaDeMoedas = mutableListOf<MoedaModel?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoedaViewHolder =
        MoedaViewHolder(
            ItemMoedaBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClick
        )

    override fun onBindViewHolder(holder: MoedaViewHolder, position: Int) {
        listaDeMoedas[position]?.let { moeda->
            holder.vincula(moeda)
            holder.itemView.setOnClickListener { onClick.invoke(moeda) }
        }
    }

    override fun getItemCount(): Int = listaDeMoedas.size

    fun refresh(newList: List<MoedaModel?>) {
        listaDeMoedas.clear()
        listaDeMoedas.addAll(newList)
        notifyDataSetChanged()
    }
}