package com.example.investimentos.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.Utils
import com.example.investimentos.databinding.ItemMoedaBinding
import com.example.investimentos.model.MoedaModel
import java.text.DecimalFormat

class MoedaViewHolder(
    private val binding: ItemMoedaBinding,
    private val onClick: (moedaModel: MoedaModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var moedaModel: MoedaModel

//        init {
//            itemView.setOnClickListener {
//                if (::moedaModel.isInitialized) {
//                    onClick(moedaModel)
//                }
//            }
//        }

    fun vincula(moedaModel: MoedaModel) {
        Utils.alteraCorDaVariacaoDaMoeda(moedaModel, binding.tvVariacaoMoeda)
        val formatador = DecimalFormat("#.##")
        binding.tvMoeda.text = moedaModel.isoMoeda
        binding.tvVariacaoMoeda.text = "${formatador.format(moedaModel.variacaoMoeda)}%"
        acessibilidade()
    }

    fun acessibilidade() {
        binding.tvMoeda.let { tvMoeda ->
            tvMoeda.contentDescription = "A variação da moeda ${tvMoeda.text} é de "
        }
    }

}