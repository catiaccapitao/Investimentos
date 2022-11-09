package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.adapter.MoedaAdapter
import com.example.investimentos.databinding.ActivityHomeBinding
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import com.example.investimentos.viewModel.MoedaViewModelFactory

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val moedaAdapter by lazy {
        MoedaAdapter()
    }

    lateinit var moedaViewModel: MoedaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraToolbar()
        inicializaViewModel()
        configuraRecyclerView()
        sincronizaMoedas()
        exibeMensagemDeFalha()
    }

    private fun configuraToolbar() {
        setSupportActionBar(binding.toolbarHome.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarHome.toolbarTituloHome.let { titulo ->
            titulo.contentDescription = "Lista de ${titulo.text}"
        }
    }

    private fun inicializaViewModel() {
        moedaViewModel = ViewModelProvider(this, MoedaViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
    }

    private fun configuraRecyclerView() {
        moedaAdapter.onClick = { moeda ->
            Intent(this, CambioActivity::class.java).let {
                it.putExtra("moeda", moeda)
                startActivity(it)
            }
        }
        binding.rvMoedas.adapter = moedaAdapter
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
