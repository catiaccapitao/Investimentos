package com.example.investimentos.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.R
import com.example.investimentos.model.MoedaModel
import java.text.DecimalFormat

class MoedaAdapter(
//    private val onClick: (MoedaModel) -> Unit,
) : RecyclerView.Adapter<MoedaViewHolder>() {

    private val listaDeMoedas = mutableListOf<MoedaModel?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoedaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_moeda, parent, false)
        return MoedaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoedaViewHolder, position: Int) {
        val moedaSelecionada = listaDeMoedas[position]
        moedaSelecionada?.let {
            holder.preencher(it)
            if (it.variacaoMoeda != null) {
                var cor = ""
                val formatador = DecimalFormat("#.##")
                holder.tvVariacaoMoeda.text = formatador.format(it.variacaoMoeda).toString()
                when {
                    it.variacaoMoeda < 0 -> { cor = "#D0021B" }
                    it.variacaoMoeda > 0 -> { cor ="#7ED321" }
                    else -> { cor = "#FFFFFFFF" }
                }
                holder.tvVariacaoMoeda.setTextColor(Color.parseColor(cor))
            }
            holder.itemView.setOnClickListener {
//                onClick.invoke(moedaSelecionada)
                Log.e("teste", "Funcionou!", )
            }
        }
    }

    override fun getItemCount(): Int = listaDeMoedas.size


    fun refresh(newList: List<MoedaModel?>) {
        listaDeMoedas.clear()
        listaDeMoedas.addAll(newList)
        notifyDataSetChanged()
    }

}