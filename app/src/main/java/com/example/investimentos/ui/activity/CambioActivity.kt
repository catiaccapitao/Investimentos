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
import com.example.investimentos.SingletonValoresSimulados.buscaValorSimuladoParaModel
import com.example.investimentos.SingletonValoresSimulados.cad
import com.example.investimentos.SingletonValoresSimulados.cny
import com.example.investimentos.SingletonValoresSimulados.eur
import com.example.investimentos.SingletonValoresSimulados.gbp
import com.example.investimentos.SingletonValoresSimulados.jpy
import com.example.investimentos.SingletonValoresSimulados.modificaValorPosOperacao
import com.example.investimentos.SingletonValoresSimulados.operacao
import com.example.investimentos.SingletonValoresSimulados.saldoDisponivel
import com.example.investimentos.SingletonValoresSimulados.usd
import com.example.investimentos.Utils
import com.example.investimentos.databinding.ActivityCambioBinding
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import com.example.investimentos.viewModel.MoedaViewModelFactory
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
        configuraToolbar()
        inicializaViewModel()
        buscaMoedaSelecionada()
    }

    override fun onResume() {
        super.onResume()
        binding.edtQuantidade.text?.clear()
        atualizaMoeda()
    }

    @SuppressLint("SetTextI18n")
    private fun atualizaMoeda() {
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel?.let { moeda ->
            preencheDados(moeda)
        }
    }

    private fun configuraToolbar() {
        setSupportActionBar(binding.toolbarCambio.toolbarCambio)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarCambio.btnVoltar.setOnClickListener {
            it.contentDescription = "Volta para tela de lista de moedas"
            finish()
        }
    }

    private fun inicializaViewModel() {
        moedaViewModel = ViewModelProvider(this, MoedaViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
    }

    private fun buscaMoedaSelecionada() {
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel.let { moeda ->
            if (moeda != null) {
                preencheDados(moeda)
                configuraEditTextQuantidade(moeda)
                configuraBotaoComprar(moeda)
                configuraBotaoVender(moeda)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun preencheDados(moeda: MoedaModel) {
        if (moeda.moedaEmCaixa == 0) {
            buscaValorSimuladoParaModel(moeda)
        }
        Utils.alteraCorDaVariacaoDaMoeda(moeda, binding.tvVariacaoMoedaCambio)
        binding.tvIsoENomeMoeda.text = "${moeda.isoMoeda} - ${moeda.nomeMoeda}"
        binding.tvVariacaoMoedaCambio.text =
            "${moeda.variacaoMoeda.toString().toBigDecimal().setScale(2, RoundingMode.UP)}%"
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
        binding.tvValorSaldoDisponivel.text =
            saldoDisponivel.toBigDecimal().setScale(2, RoundingMode.UP).toString()
        binding.tvValorMoedaEmCaixa.text =
            "${moeda.moedaEmCaixa} ${moeda.nomeMoeda} em caixa"
    }

    private fun configuraEditTextQuantidade(moeda: MoedaModel) {
        binding.edtQuantidade.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotBlank()) {
                quantidade = text.toString().toInt()
                if (quantidade > 0) {
                    habilitaBotaoComprar(moeda, quantidade)
                    halidaBotaoVender(moeda, quantidade)
                }
            } else {
                moedaViewModel.desabilitaBotao(binding.btnVender)
                moedaViewModel.desabilitaBotao(binding.btnComprar)
            }
        }
    }

    private fun habilitaBotaoComprar(moeda: MoedaModel, quantidade: Int) {
        if (moeda.valorCompra != null) {
            if (quantidade * moeda.valorCompra <= saldoDisponivel) {
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
            if (quantidade <= moeda.moedaEmCaixa) {
                moedaViewModel.habilitaBotao(binding.btnVender)
            } else {
                moedaViewModel.desabilitaBotao(binding.btnVender)
                binding.btnVender.contentDescription =
                    "Botão de venda desabilitado, você não possui essa quantidade de moedas para vender"
            }
        }
    }

    private fun configuraBotaoComprar(moeda: MoedaModel) {
        binding.btnComprar.setOnClickListener {
            moeda.moedaEmCaixa += quantidade
            modificaValorPosOperacao(moeda)
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

    private fun configuraBotaoVender(moeda: MoedaModel) {
        binding.btnVender.setOnClickListener {
            moeda.moedaEmCaixa -= quantidade
            modificaValorPosOperacao(moeda)
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
    }
}