package com.example.investimentos.retrofit

import com.example.investimentos.retrofit.service.MoedaService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInit {

    private val URL_BASE = "https://api.hgbrasil.com/"

    fun initMoedaService(): MoedaService {

        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(MoedaService::class.java)
    }
}
