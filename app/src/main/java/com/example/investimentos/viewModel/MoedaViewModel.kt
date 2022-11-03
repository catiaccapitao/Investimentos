package com.example.investimentos.viewModel

import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import com.example.investimentos.BaseViewModel
import com.example.investimentos.R
import com.example.investimentos.Utils.mapeiaIsoMoeda
import com.example.investimentos.Utils.moedasValoresSimulados
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import kotlinx.coroutines.launch

class MoedaViewModel(private val moedaRepository: MoedaRepository) : BaseViewModel() {

    val listaDeMoedas = MutableLiveData<List<MoedaModel?>>()

    fun atualizaMoedas(): MutableLiveData<List<MoedaModel?>> {
        launch {
            try {
                val call = moedaRepository.buscaTodasMoedas()
                val listaMoedas = mapeiaIsoMoeda(
                    listOf(
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
                listaDeMoedas.postValue(moedasValoresSimulados( listaMoedas))

            } catch (e: Exception) {

            }
        }
        return listaDeMoedas
    }

    fun verificaSeMoedaEstaDisponivelParaCompra(botao: AppCompatButton, valorCompra: Double?) {
        if (desabilitaBotao(botao, valorCompra)) {
            botao.contentDescription = "Botão de compra desabilitado, não é há preço de compra para esta moeda"
        }
    }

    fun verificaSeHaSaldoDisponivel(botao: AppCompatButton, saldoDisponivel: Double?) {
        if (saldoDisponivel != null) {
            if (saldoDisponivel <= 0 ) {
                botao.isEnabled = false
                botao.setBackgroundResource(R.drawable.botao_personalizado_desabilitado)
                botao.contentDescription = "Botão de compra desabilitado, você não possui saldo para comprar moedas"
            }
        }
    }

    fun verificaSeHaMoedaEmCaixa(botao: AppCompatButton, moedaEmCaixa: Int?) {
        if (moedaEmCaixa != null) {
            if (moedaEmCaixa <= 0) {
                botao.isEnabled = false
                botao.setBackgroundResource(R.drawable.botao_personalizado_desabilitado)
                botao.contentDescription = "Botão de venda desabilitado, você não possui moedas em caixa para vender"
            }
        }
    }

    fun verificaSeMoedaEstaDisponivelParaVenda(botao: AppCompatButton, valorVenda: Double?) {
        if (desabilitaBotao(botao, valorVenda)) {
            botao.contentDescription = "Botão de venda desabilitado, não é há preço de venda para esta moeda"
        }
    }

    fun desabilitaBotao(botao: AppCompatButton, valor: Double?): Boolean {
        if ((valor == null) || (valor == 0.00)) {
            botao.isEnabled = false
            botao.setBackgroundResource(R.drawable.botao_personalizado_desabilitado)
            return true
        }
        return false
    }

}