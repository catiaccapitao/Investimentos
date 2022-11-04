package com.example.investimentos.repository

import com.example.investimentos.model.DataModel
import com.example.investimentos.retrofit.RetrofitInit

class MoedaRepository {

    private val moedaApi = RetrofitInit().initMoedaApi()

    suspend fun buscaTodasMoedas(): DataModel {
        return moedaApi.buscaTodasMoedas()
    }

}