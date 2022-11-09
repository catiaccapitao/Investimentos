package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.investimentos.SingletonValoresSimulados.operacao
import com.example.investimentos.databinding.ActivityCompraEVendaBinding
import com.example.investimentos.model.MoedaModel
import java.math.RoundingMode

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
        setSupportActionBar(binding.toolbarOperacaoFinalizada.toolbarCambio)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarOperacaoFinalizada.btnVoltar.setOnClickListener {
            it.contentDescription = "Volta para tela de câmbio"
            finish()
        }
        binding.toolbarOperacaoFinalizada.toolbarTelaAnterior.text = "Câmbio"
        when (operacao) {
            "vender" -> {
                binding.toolbarOperacaoFinalizada.toolbarTituloCambio.text = "Vender"
            }
            "comprar" -> {
                binding.toolbarOperacaoFinalizada.toolbarTituloCambio.text = "Comprar"
            }
        }
    }

    private fun finalizaOperacao() {
        val quantidade = intent.getIntExtra("quantidade", 0)
        val totalOperacao = intent.getDoubleExtra("operacaoFinalizada", 0.0)
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel.let { moeda ->
            operacaoFinalizada?.let {
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

            binding.btnVoltarHome.setOnClickListener {
                Intent(this, HomeActivity::class.java).let {
                    it.putExtra("moeda", moeda)
                    finish()
                    startActivity(it)
                }
            }
        }
    }
}