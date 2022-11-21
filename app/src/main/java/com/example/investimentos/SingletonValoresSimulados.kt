package com.example.investimentos

object SingletonValoresSimulados {

    var tipoOperacao: String? = null
    var saldoDisponivel = 100.0

    var hashMapValoresSimulados = HashMap<String, Int>().apply {
        put("USD", 7)
        put("EUR", 3)
        put("GBP", 2)
        put("ARS", 0)
        put("CAD", 8)
        put("AUD", 10)
        put("JPY", 6)
        put("CNY", 1)
        put("BTC", 4)
    }

    fun pegaValorHashmap(isoMoeda: String): Int {
        var quantidadeSimulada = 0
        if (hashMapValoresSimulados.containsKey(isoMoeda)) {
            hashMapValoresSimulados.map {
                if (it.key == isoMoeda) {
                    quantidadeSimulada = it.value
                }
            }
        } else {
            quantidadeSimulada = 0
        }
        return quantidadeSimulada
    }

    fun modificaValorSimulado(
        isoMoeda: String,
        operacao: String,
        quantidade: Int
    ) {
        hashMapValoresSimulados.forEach {
            if (it.key == isoMoeda) {
                var quantidadeSimulada = it.value
                if (operacao == COMPRAR) {
                    quantidadeSimulada += quantidade
                } else {
                    quantidadeSimulada -= quantidade
                }
                hashMapValoresSimulados[it.key] = quantidadeSimulada
            }
        }
    }
}