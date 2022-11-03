package com.example.investimentos.retrofit

import com.example.investimentos.model.DataModel
import retrofit2.http.GET

interface MoedaApi {

    @GET("finance?fields=only_results,currencies&key=8d4517e5")
    suspend fun buscaTodasMoedas(): DataModel

}
