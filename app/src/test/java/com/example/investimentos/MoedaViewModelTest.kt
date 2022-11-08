package com.example.investimentos

import com.example.investimentos.model.DataModel
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class MoedaViewModelTest : BaseTest() {

    val moedaApi = mockk<MoedaRepository>(relaxUnitFun = true)
    var moedaViewModel: MoedaViewModel = MoedaViewModel(moedaApi)

    @Test
    fun deve_RetornarListaDeMoedas_QuandoBuscarListaDeMoedas() {
        val resultado = DataModel().apply {
            this.currencies.EUR = MoedaModel(
                nomeMoeda = "Euro",
                variacaoMoeda = 0.5,
                valorCompra = 5.10,
                valorVenda = 5.09,
                isoMoeda = "EUR",
                moedaEmCaixa = 0
            )
        }
        val listaEsperada = listOfNotNull(resultado.currencies.EUR)

        coEvery { moedaApi.buscaTodasMoedas() } returns resultado

        moedaViewModel.atualizaMoedas()

        assertEquals(listaEsperada, moedaViewModel.listaDeMoedas.value)
    }

    @Test
    fun deve_RetornarErro_QuandoNaoForPossivelCarregarListaDeMoedas() {

        coEvery { moedaApi.buscaTodasMoedas() } throws Exception("Não foi possível carregar as moedas!")


        moedaViewModel.atualizaMoedas()
        assertEquals(
            "Não foi possível carregar as moedas!",
            moedaViewModel.mensagemErro.value
        )


    }

}
