package com.example.investimentos

import android.graphics.Color
import android.widget.TextView
import com.example.investimentos.model.MoedaModel

object Utils {

    fun alteraCorDaVariacaoDaMoeda(moedaModel: MoedaModel, tvVariacaoMoeda: TextView) {
        val variacaoMoeda = moedaModel.variacaoMoeda
        var cor = ""
        if (variacaoMoeda != null) {
            when {
                variacaoMoeda < 0 -> cor = "#D0021B"
                variacaoMoeda > 0 -> cor = "#7ED321"
                else -> cor = "#FFFFFFFF"
            }
            tvVariacaoMoeda.setTextColor(Color.parseColor(cor))
        }
    }

    fun mapeiaIsoMoeda(moedas: List<MoedaModel?>): List<MoedaModel?> {
        return moedas.map {
            it?.apply {
                it.isoMoeda = when (it.nomeMoeda) {
                    "Dollar" -> "USD"
                    "Euro" -> "EUR"
                    "Pound Sterling" -> "GBP"
                    "Argentine Peso" -> "ARS"
                    "Canadian Dollar" -> "CAD"
                    "Australian Dollar" -> "AUD"
                    "Japanese Yen" -> "JPY"
                    "Renminbi" -> "CNY"
                    "Bitcoin" -> "BTC"
                    else -> ""
                }
            }
        }
    }
}