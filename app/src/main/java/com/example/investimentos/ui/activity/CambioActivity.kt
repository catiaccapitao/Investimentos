package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.*
import com.example.investimentos.SingletonValoresSimulados.buscaValorSimuladoParaModel
import com.example.investimentos.SingletonValoresSimulados.modificaValorPosOperacao
import com.example.investimentos.SingletonValoresSimulados.saldoDisponivel
import com.example.investimentos.SingletonValoresSimulados.tipoOperacao
import com.example.investimentos.Utils.formataMoedaBrasileira
import com.example.investimentos.Utils.formataPorcentagem
import com.example.investimentos.databinding.ActivityCambioBinding
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import com.example.investimentos.viewModel.MoedaViewModelFactory

class CambioActivity : BaseActivity() {

    private val cambioBinding by lazy {
        ActivityCambioBinding.inflate(layoutInflater)
    }

    private lateinit var moedaViewModel: MoedaViewModel
    private var moedaModel: MoedaModel? = null
    private var quantidade = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cambioBinding.root)
        setIsHeading(cambioBinding.toolbarCambio.toolbarTitulo)
        inicializaViewModel()
        buscaMoedaSelecionada()
    }

    override fun onResume() {
        super.onResume()
        cambioBinding.edtQuantidade.text?.clear()
        atualizaMoeda()
    }

    private fun atualizaMoeda() {
        moedaModel?.let { moeda ->
            preencheDados(moeda)
        }
    }

    private fun inicializaViewModel() {
        moedaViewModel =
            ViewModelProvider(
                this,
                MoedaViewModelFactory(MoedaRepository())
            )[MoedaViewModel::class.java]
    }

    private fun buscaMoedaSelecionada() {
        moedaModel = intent.getSerializableExtra(MOEDA) as? MoedaModel
        moedaModel?.let { moeda ->
            preencheDados(moeda)
            configuraEditTextQuantidade(moeda)
            criaListenersBotoes(moeda)
        }
    }

    private fun preencheDados(moedaModel: MoedaModel) {
        if (moedaModel.moedaEmCaixa == 0) buscaValorSimuladoParaModel(moedaModel)
        if (moedaModel.valorCompra == null) moedaModel.valorCompra = 0.0
        if (moedaModel.valorVenda == null) moedaModel.valorVenda = 0.0
        Utils.alteraCorDaVariacaoDaMoeda(moedaModel, cambioBinding.tvVariacaoMoedaCambio)

        cambioBinding.tvIsoMoeda.text = moedaModel.isoMoeda
        cambioBinding.tvNomeMoeda.text = moedaModel.nomeMoeda
        cambioBinding.tvVariacaoMoedaCambio.text = formataPorcentagem(moedaModel.variacaoMoeda)
        cambioBinding.tvValorCompra.text = buildString {
            append(getString(R.string.compra))
            append(formataMoedaBrasileira(moedaModel.valorCompra))
        }
        cambioBinding.tvValorVenda.text = buildString {
            append(getString(R.string.venda))
            append(formataMoedaBrasileira(moedaModel.valorVenda))
        }
        cambioBinding.tvValorSaldoDisponivel.text = buildString {
            append(getString(R.string.saldo_disponivel))
            append(formataMoedaBrasileira(saldoDisponivel))
        }
        cambioBinding.tvValorMoedaEmCaixa.text = buildString {
            append(moedaModel.moedaEmCaixa)
            append(getString(R.string.espaço))
            append(moedaModel.nomeMoeda)
            append(getString(R.string.em_caixa))
        }
    }

    private fun configuraEditTextQuantidade(moedaModel: MoedaModel) {
        cambioBinding.edtQuantidade.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotBlank()) {
                quantidade = text.toString().toInt()
                if (quantidade > 0) {
                    habilitaBotoes(moedaModel, quantidade)
                }
            } else {
                desabilitaBotoes()
            }
        }
    }

    private fun habilitaBotoes(moedaModel: MoedaModel, quantidade: Int) {
        if (quantidade * moedaModel.valorCompra!! <= saldoDisponivel) {
            moedaViewModel.defineEstadoBotao(
                true,
                cambioBinding.btnComprar,
                R.drawable.botao_personalizado
            )
        }
        if (quantidade <= moedaModel.moedaEmCaixa) {
            moedaViewModel.defineEstadoBotao(
                true,
                cambioBinding.btnVender,
                R.drawable.botao_personalizado
            )
        }
    }

    private fun desabilitaBotoes() {
        moedaViewModel.desabilitaBotao(
            cambioBinding.btnVender,
            R.drawable.botao_personalizado_desabilitado
        )
        moedaViewModel.desabilitaBotao(
            cambioBinding.btnComprar,
            R.drawable.botao_personalizado_desabilitado
        )
    }

    private fun criaListenersBotoes(moedaModel: MoedaModel) {
        cambioBinding.toolbarCambio.btnVoltar.setOnClickListener { finish() }
        cambioBinding.btnComprar.setOnClickListener { calculoCompra(moedaModel) }
        cambioBinding.btnVender.setOnClickListener { calculoVenda(moedaModel) }
    }

    private fun calculoCompra(moedaModel: MoedaModel) {
        moedaModel.moedaEmCaixa += quantidade
        modificaValorPosOperacao(moedaModel)
        val totalCompra = quantidade * moedaModel.valorCompra!!
        saldoDisponivel -= totalCompra
        finalizaOperacao(CompraEVendaActivity::class.java, COMPRAR, totalCompra)
    }

    private fun calculoVenda(moedaModel: MoedaModel) {
        moedaModel.moedaEmCaixa -= quantidade
        modificaValorPosOperacao(moedaModel)
        val totalVenda = quantidade * moedaModel.valorVenda!!
        saldoDisponivel += totalVenda
        finalizaOperacao(CompraEVendaActivity::class.java, VENDER, totalVenda)
    }

    private fun finalizaOperacao(classe: Class<*>, operacao: String, total: Double) {
        Intent(this, classe).let {
            tipoOperacao = operacao
            it.putExtra(MOEDA, moedaModel)
            it.putExtra(QUANTIDADE, quantidade)
            it.putExtra(OPERACAO_FINALIZADA, total)
            startActivity(it)
        }
    }
}