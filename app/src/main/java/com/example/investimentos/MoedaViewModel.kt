package com.example.investimentos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.retrofit.RetrofitInit
import com.google.android.material.color.MaterialColors.getColor
import kotlinx.coroutines.launch

class MoedaViewModel : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()

    fun atualizaMoedas(): MutableLiveData<List<MoedaModel?>> {
        launch {
            try {
                val moedaService = RetrofitInit().initMoedaService()
                val call = moedaService.buscaTodasMoedas()
                val listaMoedas = listOf<MoedaModel?>(
                    call.currencies.USD,
                    call.currencies.EUR,
                    call.currencies.AUD,
                    call.currencies.CAD,
                    call.currencies.ARS,
                    call.currencies.GBP,
                    call.currencies.BTC
                )
                listaDeMoedas.postValue(listaMoedas)

            } catch (e: Exception) {
                Log.e("Erro", "Erro ao buscar as moedas: ", e)
            }
        }
        return listaDeMoedas
    }

}