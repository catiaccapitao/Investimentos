package com.example.investimentos.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.SingletonValoresSimulados.ars
import com.example.investimentos.SingletonValoresSimulados.aud
import com.example.investimentos.SingletonValoresSimulados.btc
import com.example.investimentos.SingletonValoresSimulados.cad
import com.example.investimentos.SingletonValoresSimulados.cny
import com.example.investimentos.SingletonValoresSimulados.eur
import com.example.investimentos.SingletonValoresSimulados.gbp
import com.example.investimentos.SingletonValoresSimulados.jpy
import com.example.investimentos.SingletonValoresSimulados.operacao
import com.example.investimentos.SingletonValoresSimulados.saldoDisponivel
import com.example.investimentos.SingletonValoresSimulados.usd
import com.example.investimentos.Utils
import com.example.investimentos.databinding.ActivityCambioBinding
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MainViewModelFactory
import com.example.investimentos.viewModel.MoedaViewModel
import java.math.RoundingMode

class CambioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCambioBinding.inflate(layoutInflater)
    }

    lateinit var moedaViewModel: MoedaViewModel
    private var moedaModel: MoedaModel? = null
    private var quantidade = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCambio.toolbarCambio)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarCambio.btnVoltar.setOnClickListener { finish() }
        moedaViewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
        buscaMoeda()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        binding.edtQuantidade.text?.clear()
        moedaViewModel.desabilitaBotao(binding.btnVender)
        moedaViewModel.desabilitaBotao(binding.btnComprar)
        binding.tvValorMoedaEmCaixa.clearComposingText()
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel?.let { moeda ->
            buscaValorSimuladoParaModel(moeda)
//            fun Double.format(digits: Int) = "%.${digits}f".format(this)
            binding.tvValorSaldoDisponivel.text = saldoDisponivel.toBigDecimal().setScale(2, RoundingMode.UP).toString()
            binding.tvValorMoedaEmCaixa.text = "${moeda.moedaEmCaixa} Dolares em caixa"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buscaMoeda() {
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel.let { moeda ->
            if (moeda != null) {
                if (moeda.moedaEmCaixa == 0) {
                    buscaValorSimuladoParaModel(moeda)
                }
                Utils.alteraCorDaVariacaoDaMoeda(moeda, binding.tvVariacaoMoedaCambio)
                binding.tvIsoENomeMoeda.text = "${moeda.isoMoeda} - ${moeda.nomeMoeda}"
                binding.tvVariacaoMoedaCambio.text = moeda.variacaoMoeda.toString()
                if (moeda.valorCompra == null) {
                    binding.tvValorCompra.text = "0.00"
                } else {
                    binding.tvValorCompra.text = moeda.valorCompra.toString()
                }
                if (moeda.valorVenda == null) {
                    binding.tvValorVenda.text = "0.00"
                } else {
                    binding.tvValorVenda.text = moeda.valorVenda.toString()
                }
                binding.tvValorSaldoDisponivel.text = saldoDisponivel.toBigDecimal().setScale(2, RoundingMode.UP).toString()
                binding.tvValorMoedaEmCaixa.text = "${moeda.moedaEmCaixa} Dolares em caixa"


                binding.edtQuantidade.doOnTextChanged { text, start, before, count ->
                    if (text.toString().isNotBlank()) {
                        quantidade = text.toString().toInt()
                        habilitaBotaoComprar(moeda, quantidade)
                        halidaBotaoVender(moeda, quantidade)
                    }
                }

                binding.btnVender.setOnClickListener {
                    subtraiValorSimulado(moeda)
                    val totalVenda = quantidade * moeda.valorVenda!!
                    saldoDisponivel += totalVenda

                    Intent(this, CompraEVendaActivity::class.java).let {
                        operacao = "vender"
                        it.putExtra("moeda", moeda)
                        it.putExtra("quantidade", quantidade)
                        it.putExtra("operacaoFinalizada", totalVenda)
                        startActivity(it)
                    }
                }

                binding.btnComprar.setOnClickListener {
                    somaValorSimulado(moeda)
                    val totalCompra = quantidade * moeda.valorCompra!!
                    saldoDisponivel -= totalCompra

                    Intent(this, CompraEVendaActivity::class.java).let {
                        operacao = "comprar"
                        it.putExtra("moeda", moeda)
                        it.putExtra("quantidade", quantidade)
                        it.putExtra("operacaoFinalizada", totalCompra)
                        startActivity(it)
                    }
                }
            }
        }
    }

    private fun habilitaBotaoComprar(moeda: MoedaModel, quantidade: Int) {
        if (moeda.valorCompra != null) {
            if (quantidade * moeda.valorCompra < saldoDisponivel) {
                moedaViewModel.habilitaBotao(binding.btnComprar)
            } else {
                moedaViewModel.desabilitaBotao(binding.btnComprar)
                binding.btnComprar.contentDescription =
                    "Botão de compra desabilitado, saldo insuficiente para realizar essa compra"
            }
        }
    }

    private fun halidaBotaoVender(moeda: MoedaModel, quantidade: Int) {
        if (moeda.valorVenda != null) {
            if (quantidade < moeda.moedaEmCaixa) {
                moedaViewModel.habilitaBotao(binding.btnVender)
            } else {
                moedaViewModel.desabilitaBotao(binding.btnVender)
                binding.btnVender.contentDescription =
                    "Botão de venda desabilitado, você não possui essa quantidade de moedas para vender"
            }
        }
    }

    fun subtraiValorSimulado(moeda: MoedaModel) {
        when {
            moeda.isoMoeda.equals("USD") -> usd -= quantidade
            moeda.isoMoeda.equals("EUR") -> eur -= quantidade
            moeda.isoMoeda.equals("GBP") -> gbp -= quantidade
            moeda.isoMoeda.equals("ARS") -> ars -= quantidade
            moeda.isoMoeda.equals("CAD") -> cad -= quantidade
            moeda.isoMoeda.equals("AUD") -> aud -= quantidade
            moeda.isoMoeda.equals("JPY") -> jpy -= quantidade
            moeda.isoMoeda.equals("CNY") -> cny -= quantidade
            moeda.isoMoeda.equals("BTC") -> btc -= quantidade
        }
    }

    fun somaValorSimulado(moeda: MoedaModel) {
        when {
            moeda.isoMoeda.equals("USD") -> usd += quantidade
            moeda.isoMoeda.equals("EUR") -> eur += quantidade
            moeda.isoMoeda.equals("GBP") -> gbp += quantidade
            moeda.isoMoeda.equals("ARS") -> ars += quantidade
            moeda.isoMoeda.equals("CAD") -> cad += quantidade
            moeda.isoMoeda.equals("AUD") -> aud += quantidade
            moeda.isoMoeda.equals("JPY") -> jpy += quantidade
            moeda.isoMoeda.equals("CNY") -> cny += quantidade
            moeda.isoMoeda.equals("BTC") -> btc += quantidade
        }
    }

    fun buscaValorSimuladoParaModel(moeda: MoedaModel) {
        when {
            moeda.isoMoeda.equals("USD") -> moeda.moedaEmCaixa = usd
            moeda.isoMoeda.equals("EUR") -> moeda.moedaEmCaixa = eur
            moeda.isoMoeda.equals("GBP") -> moeda.moedaEmCaixa = gbp
            moeda.isoMoeda.equals("ARS") -> moeda.moedaEmCaixa = ars
            moeda.isoMoeda.equals("CAD") -> moeda.moedaEmCaixa = cad
            moeda.isoMoeda.equals("AUD") -> moeda.moedaEmCaixa = aud
            moeda.isoMoeda.equals("JPY") -> moeda.moedaEmCaixa = jpy
            moeda.isoMoeda.equals("CNY") -> moeda.moedaEmCaixa = cny
            moeda.isoMoeda.equals("BTC") -> moeda.moedaEmCaixa = btc
        }
    }

}