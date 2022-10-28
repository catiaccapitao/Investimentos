package com.example.investimentos.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.R
import com.example.investimentos.model.MoedaModel

class MoedaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvMoeda = itemView.findViewById<TextView>(R.id.tvMoeda)
     val tvVariacaoMoeda = itemView.findViewById<TextView>(R.id.tvVariacaoMoeda)

    fun preencher(moedaModel: MoedaModel) {
        tvMoeda.text = moedaModel.nomeMoeda
        tvVariacaoMoeda.text = moedaModel.variacaoMoeda.toString()
    }
}