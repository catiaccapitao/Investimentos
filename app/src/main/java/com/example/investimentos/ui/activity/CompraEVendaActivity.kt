package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.investimentos.SingletonValoresSimulados.operacao
import com.example.investimentos.Utils.increaseTouch
import com.example.investimentos.databinding.ActivityCompraEVendaBinding
import com.example.investimentos.model.MoedaModel
import java.math.RoundingMode

private const val CAMBIO = "Câmbio"
private const val VENDER = "Vender"
private const val COMPRAR = "Comprar"

class CompraEVendaActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCompraEVendaBinding.inflate(layoutInflater)
    }
    private var moedaModel: MoedaModel? = null
    private val operacaoFinalizada = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraToolbar()
        finalizaOperacao()
    }

    private fun configuraToolbar() {
        setSupportActionBar(binding.toolbarOperacaoFinalizada.toolbarPrincipal)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarOperacaoFinalizada.btnVoltar.contentDescription = "Volta para tela de câmbio"
        binding.toolbarOperacaoFinalizada.toolbarTelaAnterior.text = CAMBIO
        binding.toolbarOperacaoFinalizada.btnVoltar.setOnClickListener { finish() }
        increaseTouch(binding.toolbarOperacaoFinalizada.btnVoltar, 150F)
        when (operacao) {
            "vender" -> {
                binding.toolbarOperacaoFinalizada.toolbarTitulo.text = VENDER
                binding.toolbarOperacaoFinalizada.toolbarTitulo.contentDescription =
                    "Tela de Venda Finalizada"
            }
            "comprar" -> {
                binding.toolbarOperacaoFinalizada.toolbarTitulo.text = COMPRAR
                binding.toolbarOperacaoFinalizada.toolbarTitulo.contentDescription =
                    "Tela de Compra Finalizada"
            }
        }
    }

    private fun finalizaOperacao() {
        val quantidade = intent.getIntExtra("quantidade", 0)
        val totalOperacao = intent.getDoubleExtra("operacaoFinalizada", 0.0)
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel.let { moeda ->
            operacaoFinalizada.let {
                it.append(binding.tvOperacaoFinalizada.text)
                    .append("Parabéns! \n Você acabou de $operacao\n")
                    .append("$quantidade ${moeda?.isoMoeda} - ${moeda?.nomeMoeda},\n")
                    .append("totalizando \n")
                    .append(
                        "R$ ${totalOperacao.toBigDecimal().setScale(2, RoundingMode.UP)}"
                    )
                    .toString()
                binding.tvOperacaoFinalizada.text = it
            }
            configuraBotaoHome(moeda)
        }
    }

    private fun configuraBotaoHome(moedaModel: MoedaModel?) {
        binding.btnVoltarHome.contentDescription = "Volta para a tela lista de moedas"
        binding.btnVoltarHome.setOnClickListener {
            Intent(this, HomeActivity::class.java).let {
                it.putExtra("moeda", moedaModel)
                finish()
                startActivity(it)
            }
        }
    }
}