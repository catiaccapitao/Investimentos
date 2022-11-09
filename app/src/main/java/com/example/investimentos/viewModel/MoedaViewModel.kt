package com.example.investimentos.viewModel

import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import com.example.investimentos.BaseViewModel
import com.example.investimentos.R
import com.example.investimentos.Utils.mapeiaIsoMoeda
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import kotlinx.coroutines.launch

class MoedaViewModel(
    private val moedaRepository: MoedaRepository
) : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()
    val mensagemErro = MutableLiveData<String>()

    fun atualizaMoedas(): MutableLiveData<List<MoedaModel?>> {
        launch {
            try {
                val call = moedaRepository.buscaTodasMoedas()
                val listaMoedas = mapeiaIsoMoeda(
                    listOfNotNull(
                        call.currencies.USD,
                        call.currencies.EUR,
                        call.currencies.GBP,
                        call.currencies.ARS,
                        call.currencies.CAD,
                        call.currencies.AUD,
                        call.currencies.JPY,
                        call.currencies.CNY,
                        call.currencies.BTC
                    )
                )
                listaDeMoedas.postValue(listaMoedas)
            } catch (e: Exception) {
                mensagemErro.postValue("Não foi possível carregar as moedas!")
            }
        }
        return listaDeMoedas
    }

    fun habilitaBotao(botao: AppCompatButton) {
        botao.isEnabled = true
        botao.setBackgroundResource(R.drawable.botao_personalizado)
    }

    fun desabilitaBotao(botao: AppCompatButton) {
        botao.isEnabled = false
        botao.setBackgroundResource(R.drawable.botao_personalizado_desabilitado)
    }
}