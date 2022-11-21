package com.example.investimentos.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.Utils
import com.example.investimentos.Utils.formataPorcentagem
import com.example.investimentos.databinding.ItemMoedaBinding
import com.example.investimentos.model.MoedaModel

class MoedaViewHolder(
    private val binding: ItemMoedaBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun vincula(moedaModel: MoedaModel) {
        Utils.alteraCorDaVariacaoDaMoeda(moedaModel, binding.tvVariacaoMoeda)
        binding.tvMoeda.text = moedaModel.isoMoeda
        binding.tvVariacaoMoeda.text = formataPorcentagem(moedaModel.variacaoMoeda)
        acessibilidade()
    }

    private fun acessibilidade() {
        binding.tvMoeda.let { tvMoeda ->
            tvMoeda.contentDescription = "A variação da moeda ${tvMoeda.text} é de "
        }
    }

}