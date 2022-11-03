package com.example.investimentos.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.SingletonValoresSimulados.saldoDisponivel
import com.example.investimentos.Utils
import com.example.investimentos.databinding.ActivityCambioBinding
import com.example.investimentos.model.MoedaModel
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MainViewModelFactory
import com.example.investimentos.viewModel.MoedaViewModel

class CambioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCambioBinding.inflate(layoutInflater)
    }

    lateinit var moedaViewModel: MoedaViewModel
    private var moedaModel: MoedaModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        moedaViewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
        buscaMoeda()



        binding.btnVender.setOnClickListener {
            Log.e("teste", "venda realizada")
//            Intent(this, OperacaoFinalizadaActivity::class.java).let { telaVenda ->
//                startActivity(telaVenda)
//            }
        }

        binding.btnComprar.setOnClickListener {
            Log.e("teste", "compra realizada")
//            Intent(this, OperacaoFinalizadaActivity::class.java).let {
//                startActivity(it)
//            }
        }

    }

    private fun buscaMoeda() {
        moedaModel = intent.getSerializableExtra("moeda") as? MoedaModel
        moedaModel.let { moeda ->
            if (moeda != null) {
                Utils.alteraCorDaVariacaoDaMoeda(moeda, binding.tvVariacaoMoedaCambio)
                validaBotoes(moeda)
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
                binding.tvValorSaldoDisponivel.text = saldoDisponivel.toString()
                binding.tvValorMoedaEmCaixa.text = "${moeda.moedaEmCaixa} Dolares em caixa"

//                binding.edtQuantidade
            }
        }
    }

    fun validaBotoes(moeda: MoedaModel){
        moedaViewModel.verificaSeMoedaEstaDisponivelParaCompra(binding.btnComprar, moeda.valorCompra)
        moedaViewModel.verificaSeMoedaEstaDisponivelParaVenda(binding.btnVender, moeda.valorVenda)
        moedaViewModel.verificaSeHaSaldoDisponivel(binding.btnComprar, saldoDisponivel)
        moedaViewModel.verificaSeHaMoedaEmCaixa(binding.btnVender, moeda.moedaEmCaixa)
    }
}