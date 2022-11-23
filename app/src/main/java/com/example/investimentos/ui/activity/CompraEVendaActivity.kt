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
        tipoOperacao?.let {
            configuraToolbar(
                compraEVendaBinding.toolbarOperacaoFinalizada.toolbarTitulo,
                compraEVendaBinding.toolbarOperacaoFinalizada.btnVoltar,
                it
            )
        }
        modificaNomeTelaAnteriorToolbar(compraEVendaBinding.toolbarOperacaoFinalizada.toolbarTelaAnterior, CAMBIO)
        finalizaOperacao()
        configuraBotaoHome()
    }

    private fun finalizaOperacao() {
        val quantidade = intent.getIntExtra(QUANTIDADE, 0)
        val totalOperacao = intent.getDoubleExtra(OPERACAO_FINALIZADA, 0.0)
        moedaModel = intent.getSerializableExtra(MOEDA) as? MoedaModel
        moedaModel.let { moeda ->
            operacaoFinalizada.let {
                if (tipoOperacao == COMPRAR) {
                    it.append(compraEVendaBinding.tvOperacaoFinalizada.text)
                        .append(
                            getString(R.string.parabens_voce_acabou_de_comprar),
                            quantidade,
                            getString(R.string.espaço),
                            moeda?.isoMoeda,
                            getString(R.string.hifen),
                            moeda?.nomeMoeda,
                            getString(R.string.totalizando),
                            formataMoedaBrasileira(totalOperacao)
                        ).toString()
                } else if (tipoOperacao == VENDER) {
                    it.append(compraEVendaBinding.tvOperacaoFinalizada.text)
                        .append(
                            getString(R.string.parabens_voce_acabou_de_vender),
                            quantidade,
                            getString(R.string.espaço),
                            moeda?.isoMoeda,
                            getString(R.string.hifen),
                            moeda?.nomeMoeda,
                            getString(R.string.totalizando),
                            formataMoedaBrasileira(totalOperacao)
                        ).toString()
                }
                compraEVendaBinding.tvOperacaoFinalizada.text = it
            }
        }
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