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
        hashMapValoresSimulados[isoMoeda].let { quantidade ->
            if (quantidade != null) return quantidade
            else return 0
        }
    }

    fun modificaValorSimulado(
        isoMoeda: String,
        operacao: String,
        quantidade: Int
    ) {
        var quantidadeSimulada = hashMapValoresSimulados[isoMoeda]
        if (quantidadeSimulada != null) {
            if (operacao == COMPRAR) {
                quantidadeSimulada += quantidade
            } else {
                quantidadeSimulada -= quantidade
            }
            hashMapValoresSimulados[isoMoeda] = quantidadeSimulada
        }
    }
}