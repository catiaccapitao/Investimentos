package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.investimentos.*
import com.example.investimentos.SingletonValoresSimulados.modificaValorSimulado
import com.example.investimentos.SingletonValoresSimulados.pegaValorHashmap
import com.example.investimentos.SingletonValoresSimulados.saldoDisponivel
import com.example.investimentos.SingletonValoresSimulados.tipoOperacao
import com.example.investimentos.Utils.defineEstadoBotao
import com.example.investimentos.Utils.desabilitaBotao
import com.example.investimentos.Utils.formataMoedaBrasileira
import com.example.investimentos.Utils.formataPorcentagem
import com.example.investimentos.databinding.ActivityCambioBinding
import com.example.investimentos.model.MoedaModel

class CambioActivity : BaseActivity() {

    private val cambioBinding by lazy {
        ActivityCambioBinding.inflate(layoutInflater)
    }

    private var moedaModel: MoedaModel? = null
    private var quantidade = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cambioBinding.root)
        configuraToolbar(
            cambioBinding.toolbarCambio.toolbarTitulo,
            cambioBinding.toolbarCambio.btnVoltar,
            CAMBIO
        )
        modificaNomeTelaAnteriorToolbar(cambioBinding.toolbarCambio.toolbarTelaAnterior, MOEDAS)
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

    private fun buscaMoedaSelecionada() {
        moedaModel = intent.getSerializableExtra(MOEDA) as? MoedaModel
        moedaModel?.let { moeda ->
            preencheDados(moeda)
            configuraEditTextQuantidade(moeda)
            criaListenersBotoes(moeda)
        }
    }

    private fun preencheDados(moedaModel: MoedaModel) {
        if (moedaModel.valorCompra == null) moedaModel.valorCompra = 0.0
        if (moedaModel.valorVenda == null) moedaModel.valorVenda = 0.0
        Utils.alteraCorDaVariacaoDaMoeda(moedaModel, cambioBinding.tvVariacaoMoedaCambio)

        cambioBinding.tvIsoMoeda.text = moedaModel.isoMoeda
        cambioBinding.tvNomeMoeda.text = moedaModel.nomeMoeda
        cambioBinding.tvVariacaoMoedaCambio.text = formataPorcentagem(moedaModel.variacaoMoeda)
        cambioBinding.tvValorCompra.text = buildString {
            append(
                getString(R.string.compra),
                formataMoedaBrasileira(moedaModel.valorCompra)
            )
        }
        cambioBinding.tvValorVenda.text = buildString {
            append(
                getString(R.string.venda),
                formataMoedaBrasileira(moedaModel.valorVenda)
            )
        }
        cambioBinding.tvValorSaldoDisponivel.text = buildString {
            append(
                getString(R.string.saldo_disponivel),
                formataMoedaBrasileira(saldoDisponivel)
            )
        }
        cambioBinding.tvValorMoedaEmCaixa.text = buildString {
            append(
                pegaValorHashmap(moedaModel.isoMoeda),
                getString(R.string.espaço),
                moedaModel.nomeMoeda,
                getString(R.string.em_caixa)
            )
        }
    }

    private fun configuraEditTextQuantidade(moedaModel: MoedaModel) {
        cambioBinding.edtQuantidade.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotBlank()) {
                quantidade = text.toString().toInt()
                if (quantidade > 0) {
                    habilitaBotoes(moedaModel, quantidade, moedaModel.isoMoeda)
                }
            } else {
                desabilitaBotoes()
            }
        }
    }

    private fun habilitaBotoes(moedaModel: MoedaModel, quantidade: Int, isoMoeda: String) {
        moedaModel.valorCompra?.let { valorCompra ->
            if (quantidade * valorCompra <= saldoDisponivel) {
                defineEstadoBotao(
                    true,
                    cambioBinding.btnComprar,
                    R.drawable.botao_personalizado
                )
            }
        }
        moedaModel.valorVenda?.let { valorVenda ->
            if (quantidade <= pegaValorHashmap(isoMoeda) && valorVenda > 0) {
                defineEstadoBotao(
                    true,
                    cambioBinding.btnVender,
                    R.drawable.botao_personalizado
                )
            }
        }
    }

    private fun desabilitaBotoes() {
        desabilitaBotao(cambioBinding.btnVender, R.drawable.botao_personalizado_desabilitado)
        desabilitaBotao(cambioBinding.btnComprar, R.drawable.botao_personalizado_desabilitado)
    }

    private fun criaListenersBotoes(moedaModel: MoedaModel) {
        cambioBinding.btnComprar.setOnClickListener { calculoCompra(moedaModel) }
        cambioBinding.btnVender.setOnClickListener { calculoVenda(moedaModel) }
    }

    private fun calculoCompra(moedaModel: MoedaModel) {
        moedaModel.valorCompra?.let { valorCompra ->
            modificaValorSimulado(moedaModel.isoMoeda, COMPRAR, quantidade)
            val totalCompra = quantidade * valorCompra
            saldoDisponivel -= totalCompra
            finalizaOperacao(CompraEVendaActivity::class.java, COMPRAR, totalCompra)
        }
    }

    private fun calculoVenda(moedaModel: MoedaModel) {
        moedaModel.valorVenda?.let { valorVenda ->
            modificaValorSimulado(moedaModel.isoMoeda, VENDER, quantidade)
            val totalVenda = quantidade * valorVenda
            saldoDisponivel += totalVenda
            finalizaOperacao(CompraEVendaActivity::class.java, VENDER, totalVenda)
        }
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