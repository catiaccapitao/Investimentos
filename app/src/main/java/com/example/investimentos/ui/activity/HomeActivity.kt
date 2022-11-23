package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.investimentos.MOEDA
import com.example.investimentos.MOEDAS
import com.example.investimentos.R
import com.example.investimentos.adapter.MoedaAdapter
import com.example.investimentos.databinding.ActivityHomeBinding

class HomeActivity : BaseActivity() {

    private val homeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val moedaAdapter by lazy {
        MoedaAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(homeBinding.root)
        configuraToolbar(
            homeBinding.toolbarHome.toolbarTitulo,
            homeBinding.toolbarHome.btnVoltar,
            MOEDAS
        )
        modificaNomeTelaAnteriorToolbar(homeBinding.toolbarHome.toolbarTelaAnterior, getString(R.string.espaço))
        inicializaViewModel()
        configuraRecyclerView()
        sincronizaMoedas()
        exibeMensagemDeFalha()
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
