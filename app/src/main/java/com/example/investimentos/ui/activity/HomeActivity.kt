package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.MOEDA
import com.example.investimentos.adapter.MoedaAdapter
import com.example.investimentos.databinding.ActivityHomeBinding
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import com.example.investimentos.viewModel.MoedaViewModelFactory

class HomeActivity : BaseActivity() {

    private val homeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val moedaAdapter by lazy {
        MoedaAdapter()
    }

    private lateinit var moedaViewModel: MoedaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(homeBinding.root)
        setIsHeading(homeBinding.toolbarHome.toolbarTitulo)
        inicializaViewModel()
        configuraRecyclerView()
        sincronizaMoedas()
        exibeMensagemDeFalha()
    }

    private fun inicializaViewModel() {
        moedaViewModel =
            ViewModelProvider(
                this,
                MoedaViewModelFactory(MoedaRepository())
            )[MoedaViewModel::class.java]
    }

    private fun configuraRecyclerView() {
        moedaAdapter.onClick = { moeda ->
            Intent(this, CambioActivity::class.java).let {
                it.putExtra(MOEDA, moeda)
                startActivity(it)
            }
        }
        homeBinding.rvMoedas.adapter = moedaAdapter
    }

    private fun sincronizaMoedas() {
        moedaViewModel.listaDeMoedas.observe(this) {
            moedaAdapter.refresh(it)
        }
        moedaViewModel.atualizaMoedas()
    }

    private fun exibeMensagemDeFalha() {
        moedaViewModel.mensagemErro.observe(this) { mensagem ->
            Toast.makeText(applicationContext, mensagem, Toast.LENGTH_LONG).show()
        }
    }
}
