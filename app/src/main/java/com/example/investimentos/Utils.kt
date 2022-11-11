package com.example.investimentos

import android.graphics.Color
import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.investimentos.model.MoedaModel

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

    @BindingAdapter("increaseTouch")
    fun increaseTouch(view: View, value: Float) {
        val parent = view.parent
        (parent as View).post {
            val rect = Rect()
            view.getHitRect(rect)
            val intValue = value.toInt()
            rect.right += intValue
            parent.touchDelegate = TouchDelegate(rect, view)
        }
    }
}