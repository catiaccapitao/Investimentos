package com.example.investimentos

import com.example.investimentos.model.MoedaModel

object SingletonValoresSimulados {

    var operacao: String? = null
    var saldoDisponivel = 10.0
    var usd = 6
    var eur = 1
    var gbp = 0
    var ars = 3
    var cad = 2
    var aud = 0
    var jpy = 4
    var cny = 5
    var btc = 2

    fun buscaValorSimuladoParaModel(moeda: MoedaModel) {
        when {
            moeda.isoMoeda.equals("USD") -> moeda.moedaEmCaixa = usd
            moeda.isoMoeda.equals("EUR") -> moeda.moedaEmCaixa = eur
            moeda.isoMoeda.equals("GBP") -> moeda.moedaEmCaixa = gbp
            moeda.isoMoeda.equals("ARS") -> moeda.moedaEmCaixa = ars
            moeda.isoMoeda.equals("CAD") -> moeda.moedaEmCaixa = cad
            moeda.isoMoeda.equals("AUD") -> moeda.moedaEmCaixa = aud
            moeda.isoMoeda.equals("JPY") -> moeda.moedaEmCaixa = jpy
            moeda.isoMoeda.equals("CNY") -> moeda.moedaEmCaixa = cny
            moeda.isoMoeda.equals("BTC") -> moeda.moedaEmCaixa = btc
        }
    }

    fun modificaValorPosOperacao(moeda: MoedaModel) {
        when {
            moeda.isoMoeda.equals("USD") -> usd = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("EUR") -> eur = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("GBP") -> gbp = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("ARS") -> ars = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("CAD") -> cad = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("AUD") -> aud = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("JPY") -> jpy = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("CNY") -> cny = moeda.moedaEmCaixa
            moeda.isoMoeda.equals("BTC") -> btc = moeda.moedaEmCaixa
        }
    }

}