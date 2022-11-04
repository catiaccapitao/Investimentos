package com.example.investimentos

import androidx.lifecycle.MutableLiveData
import com.example.investimentos.model.DataModel
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.retrofit.RetrofitInit
import com.example.investimentos.viewModel.MoedaViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class MoedaViewModelTest : BaseTest() {

    val api = mockk<MoedaRepository>(relaxUnitFun = true)
    var viewModel: MoedaViewModel = MoedaViewModel(api)


    @MockK
    lateinit var moedaModel: MoedaModel


    @Test
    fun deveRetornarMoedasDaApi1() {
        val resultado = DataModel().apply {
            this.currencies.USD = MoedaModel(
                nomeMoeda = "Dollar",
                variacaoMoeda = 0.0,
                valorCompra = 1.0,
                valorVenda = 2.50,
                isoMoeda = "USD"
            )
        }
        val listaEsperada = listOf<MoedaModel?>( resultado.currencies.USD,
            resultado.currencies.EUR,
            resultado.currencies.CAD,
            resultado.currencies.GBP,
            resultado.currencies.ARS,
            resultado.currencies.AUD,
            resultado.currencies.JPY,
            resultado.currencies.CNY,
            resultado.currencies.BTC)

        coEvery { api.buscaTodasMoedas() } returns resultado

        viewModel.atualizaMoedas()

        Assert.assertEquals(listaEsperada, viewModel.listaDeMoedas.value)


    }

}
