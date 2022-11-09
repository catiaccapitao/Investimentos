package com.example.investimentos.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.Utils
import com.example.investimentos.databinding.ItemMoedaBinding
import com.example.investimentos.model.MoedaModel
import java.math.RoundingMode

class MoedaViewHolder(
    private val binding: ItemMoedaBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun vincula(moedaModel: MoedaModel) {
        Utils.alteraCorDaVariacaoDaMoeda(moedaModel, binding.tvVariacaoMoeda)
        binding.tvMoeda.text = moedaModel.isoMoeda
        binding.tvVariacaoMoeda.text =
            "${moedaModel.variacaoMoeda.toString().toBigDecimal().setScale(2, RoundingMode.UP)}%"
        acessibilidade()
    }

    fun acessibilidade() {
        binding.tvMoeda.let { tvMoeda ->
            tvMoeda.contentDescription = "A variação da moeda ${tvMoeda.text} é de "
        }
    }

}