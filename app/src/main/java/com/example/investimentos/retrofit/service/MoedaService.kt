package com.example.investimentos.retrofit.service

import com.example.investimentos.model.Data
import retrofit2.http.GET

interface MoedaService {

    @GET("finance?fields=only_results,currencies&key=8d4517e5")
    suspend fun buscaTodasMoedas(): Data


}