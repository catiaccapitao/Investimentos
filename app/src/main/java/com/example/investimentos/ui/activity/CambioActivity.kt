package com.example.investimentos.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.SingletonValoresSimulados.buscaValorSimuladoParaModel
import com.example.investimentos.SingletonValoresSimulados.modificaValorPosOperacao
import com.example.investimentos.SingletonValoresSimulados.operacao
import com.example.investimentos.SingletonValoresSimulados.saldoDisponivel
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
        setSupportActionBar(binding.toolbarCambio.toolbarPrincipal)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarCambio.btnVoltar.setOnClickListener { finish() }
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
                acessibilidade(moeda)
                configuraEditTextQuantidade(moeda)
                configuraBotaoComprar(moeda)
                configuraBotaoVender(moeda)
            }
        }
    }

    fun acessibilidade(moedaModel: MoedaModel) {
        binding.toolbarCambio.btnVoltar.contentDescription = "Volta para tela de lista de moedas"
        binding.toolbarCambio.toolbarTitulo.contentDescription = "Tela de Câmbio"
        binding.tvIsoENomeMoeda.contentDescription = moedaModel.nomeMoeda
        binding.tvVariacaoMoedaCambio.contentDescription = "A variação dessa moeda é " +
                "${moedaModel.variacaoMoeda.toString().toBigDecimal().setScale(2, RoundingMode.UP)}%"
        binding.tvValorCompra.contentDescription = "O preço de compra é " +
                "${moedaModel.valorCompra.toString().toBigDecimal().setScale(2, RoundingMode.UP)} reais"
        binding.tvValorVenda.contentDescription = "O preço de venda é " +
                "${moedaModel.valorVenda.toString().toBigDecimal().setScale(2, RoundingMode.UP)} reais"
        binding.tvValorSaldoDisponivel.contentDescription = "O saldo disponível é " +
                "${saldoDisponivel.toString().toBigDecimal().setScale(2, RoundingMode.UP)} reais"
        binding.tvValorMoedaEmCaixa.contentDescription = "Você tem ${moedaModel.moedaEmCaixa} ${moedaModel.isoMoeda} em caixa"
    }

    @SuppressLint("SetTextI18n")
    private fun preencheDados(moedaModel: MoedaModel) {
        if (moedaModel.moedaEmCaixa == 0) {
            buscaValorSimuladoParaModel(moedaModel)
        }
        Utils.alteraCorDaVariacaoDaMoeda(moedaModel, binding.tvVariacaoMoedaCambio)
        binding.tvIsoENomeMoeda.text = "${moedaModel.isoMoeda} - ${moedaModel.nomeMoeda}"
        binding.tvVariacaoMoedaCambio.text =
            "${moedaModel.variacaoMoeda.toString().toBigDecimal().setScale(2, RoundingMode.UP)}%"
        if (moedaModel.valorCompra == null) {
            binding.tvValorCompra.text = "0.00"
        } else {
            binding.tvValorCompra.text = "${moedaModel.valorCompra.toString().toBigDecimal().setScale(2, RoundingMode.UP)}"
        }
        if (moedaModel.valorVenda == null) {
            binding.tvValorVenda.text = "0.00"
        } else {
            binding.tvValorVenda.text = "${moedaModel.valorVenda.toString().toBigDecimal().setScale(2, RoundingMode.UP)}"
        }
        binding.tvValorSaldoDisponivel.text =
            saldoDisponivel.toBigDecimal().setScale(2, RoundingMode.UP).toString()
        binding.tvValorMoedaEmCaixa.text =
            "${moedaModel.moedaEmCaixa} ${moedaModel.nomeMoeda} em caixa"
    }

    private fun configuraEditTextQuantidade(moedaModel: MoedaModel) {
        binding.edtQuantidade.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotBlank()) {
                quantidade = text.toString().toInt()
                if (quantidade > 0) {
                    habilitaBotaoComprar(moedaModel, quantidade)
                    halidaBotaoVender(moedaModel, quantidade)
                }
            } else {
                moedaViewModel.desabilitaBotao(binding.btnVender)
                moedaViewModel.desabilitaBotao(binding.btnComprar)
            }
        }
    }

    private fun habilitaBotaoComprar(moedaModel: MoedaModel, quantidade: Int) {
        if (moedaModel.valorCompra != null) {
            if (quantidade * moedaModel.valorCompra <= saldoDisponivel) {
                moedaViewModel.habilitaBotao(binding.btnComprar)
                binding.btnComprar.contentDescription = "Botão de compra habilitado"
            } else {
                moedaViewModel.desabilitaBotao(binding.btnComprar)
                binding.btnComprar.contentDescription =
                    "Botão de compra desabilitado, saldo insuficiente para realizar essa compra"
            }
        }
    }

    private fun halidaBotaoVender(moedaModel: MoedaModel, quantidade: Int) {
        if (moedaModel.valorVenda != null) {
            if (quantidade <= moedaModel.moedaEmCaixa) {
                moedaViewModel.habilitaBotao(binding.btnVender)
                binding.btnVender.contentDescription = "Botão de venda habilitado"
            } else {
                moedaViewModel.desabilitaBotao(binding.btnVender)
                binding.btnVender.contentDescription =
                    "Botão de venda desabilitado, você não possui essa quantidade de moedas para vender"
            }
        }
    }

    private fun configuraBotaoComprar(moedaModel: MoedaModel) {
        binding.btnComprar.setOnClickListener {
            moedaModel.moedaEmCaixa += quantidade
            modificaValorPosOperacao(moedaModel)
            val totalCompra = quantidade * moedaModel.valorCompra!!
            saldoDisponivel -= totalCompra

            Intent(this, CompraEVendaActivity::class.java).let {
                operacao = "comprar"
                it.putExtra("moeda", moedaModel)
                it.putExtra("quantidade", quantidade)
                it.putExtra("operacaoFinalizada", totalCompra)
                startActivity(it)
            }
        }
    }

    private fun configuraBotaoVender(moedaModel: MoedaModel) {
        binding.btnVender.setOnClickListener {
            moedaModel.moedaEmCaixa -= quantidade
            modificaValorPosOperacao(moedaModel)
            val totalVenda = quantidade * moedaModel.valorVenda!!
            saldoDisponivel += totalVenda

            Intent(this, CompraEVendaActivity::class.java).let {
                operacao = "vender"
                it.putExtra("moeda", moedaModel)
                it.putExtra("quantidade", quantidade)
                it.putExtra("operacaoFinalizada", totalVenda)
                startActivity(it)
            }
        }
    }
}