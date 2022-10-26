package com.example.investimentos.retrofit.service

import com.example.investimentos.model.Data
import com.example.investimentos.model.MoedaModel
import retrofit2.Call
import retrofit2.http.GET
import java.util.Currency

interface MoedaService {

    @GET("finance?fields=only_results,currencies&key=") // adicionem a chave de vcs no final
    suspend fun buscaTodasMoedas(): Data


}
