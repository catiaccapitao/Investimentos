package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import com.example.investimentos.*
import com.example.investimentos.SingletonValoresSimulados.tipoOperacao
import com.example.investimentos.Utils.formataMoedaBrasileira
import com.example.investimentos.databinding.ActivityCompraEVendaBinding
import com.example.investimentos.model.MoedaModel


class CompraEVendaActivity : BaseActivity() {

    private val compraEVendaBinding by lazy {
        ActivityCompraEVendaBinding.inflate(layoutInflater)
    }
    private var moedaModel: MoedaModel? = null
    private val operacaoFinalizada = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(compraEVendaBinding.root)
        setIsHeading(compraEVendaBinding.toolbarOperacaoFinalizada.toolbarTitulo)
        finalizaOperacao()
        defineTituloToolbar()
        criaListenersBotoes()
    }

    private fun defineTituloToolbar() {
        when (tipoOperacao) {
            VENDER -> {
                compraEVendaBinding.toolbarOperacaoFinalizada.toolbarTitulo.text = VENDER
            }
            COMPRAR -> {
                compraEVendaBinding.toolbarOperacaoFinalizada.toolbarTitulo.text = COMPRAR
            }
        }
    }

    private fun finalizaOperacao() {
        val quantidade = intent.getIntExtra(QUANTIDADE, 0)
        val totalOperacao = intent.getDoubleExtra(OPERACAO_FINALIZADA, 0.0)
        moedaModel = intent.getSerializableExtra(MOEDA) as? MoedaModel
        moedaModel.let { moeda ->
            operacaoFinalizada.let {
                if (tipoOperacao == COMPRAR) {
                    it.append(compraEVendaBinding.tvOperacaoFinalizada.text)
                        .append(getString(R.string.parabens_voce_acabou_de_comprar))
                        .append(quantidade)
                        .append(getString(R.string.espaço))
                        .append(moeda?.isoMoeda)
                        .append(getString(R.string.hifen))
                        .append(moeda?.nomeMoeda)
                        .append(getString(R.string.totalizando))
                        .append(formataMoedaBrasileira(totalOperacao))
                        .toString()
                } else if (tipoOperacao == VENDER) {
                    it.append(compraEVendaBinding.tvOperacaoFinalizada.text)
                        .append(getString(R.string.parabens_voce_acabou_de_vender))
                        .append(quantidade)
                        .append(getString(R.string.espaço))
                        .append(moeda?.isoMoeda)
                        .append(getString(R.string.hifen))
                        .append(moeda?.nomeMoeda)
                        .append(getString(R.string.totalizando))
                        .append(formataMoedaBrasileira(totalOperacao))
                        .toString()
                }
                compraEVendaBinding.tvOperacaoFinalizada.text = it
            }
        }
    }

    private fun criaListenersBotoes() {
        compraEVendaBinding.toolbarOperacaoFinalizada.btnVoltar.setOnClickListener { finish() }
        configuraBotaoHome()
    }

    private fun configuraBotaoHome() {
        compraEVendaBinding.btnVoltarHome.contentDescription =
            getString(R.string.volta_para_home_moedas)
        compraEVendaBinding.btnVoltarHome.setOnClickListener {
            Intent(this, HomeActivity::class.java).let {
                finish()
                startActivity(it)
            }
        }
    }
}