package com.example.investimentos

import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.investimentos.model.MoedaModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Utils {

    fun alteraCorDaVariacaoDaMoeda(moedaModel: MoedaModel, tvVariacaoMoeda: TextView) {
        val variacaoMoeda = moedaModel.variacaoMoeda
        val cor: String
        if (variacaoMoeda != null) {
            cor = when {
                variacaoMoeda < 0 -> "#D0021B"
                variacaoMoeda > 0 -> "#7ED321"
                else -> "#FFFFFFFF"
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

    fun formataMoedaBrasileira(valor: Double?): String {
        return DecimalFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
    }

    fun formataPorcentagem(valor: Double?): String {
        val formatador = DecimalFormat()
        formatador.decimalFormatSymbols = DecimalFormatSymbols(Locale("pt", "BR"))
        formatador.minimumFractionDigits = 2
        formatador.maximumFractionDigits = 2
        return "${formatador.format(valor)}%"
    }

    fun habilitaBotao(botao: AppCompatButton, drawableBotao: Int) {
        botao.isEnabled = true
        botao.setBackgroundResource(drawableBotao)
    }

    fun desabilitaBotao(botao: AppCompatButton, drawableBotao: Int) {
        botao.isEnabled = false
        botao.setBackgroundResource(drawableBotao)
    }
}