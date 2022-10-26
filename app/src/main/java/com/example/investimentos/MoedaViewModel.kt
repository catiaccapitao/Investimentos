package com.example.investimentos

import androidx.lifecycle.MutableLiveData
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.retrofit.RetrofitInit
import kotlinx.coroutines.launch

class MoedaViewModel : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()

    fun atualizaMoedas() {
        launch {
            try {
                val moedaService = RetrofitInit().initMoedaService()
                val call = moedaService.buscaTodasMoedas()

                val listaMoedas = listOf<MoedaModel?>(call.currencies.USD, call.currencies.EUR)

                listaDeMoedas.postValue(listaMoedas)
            } catch (e: Exception) {

            }
        }
    }
}