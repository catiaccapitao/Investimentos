package com.example.investimentos.retrofit.service

import com.example.investimentos.model.Data
import retrofit2.http.GET

interface MoedaService {

    @GET("finance?fields=only_results,currencies&key=") // adicionem a chave de vcs no final
    suspend fun buscaTodasMoedas(): Data


}
