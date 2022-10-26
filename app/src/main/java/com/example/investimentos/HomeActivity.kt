package com.example.investimentos

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentos.adapter.MoedaAdapter

class HomeActivity : AppCompatActivity() {

    lateinit var rvMoedas: RecyclerView
    lateinit var moedaViewModel: MoedaViewModel
    private val moedaAdapter = MoedaAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        moedaViewModel = ViewModelProvider(this)[MoedaViewModel::class.java]

        configuraRecyclerView()

        moedaViewModel.listaDeMoedas.observe(this){
            it.firstNotNullOf {moeda ->
                Toast.makeText(this, moeda.toString(), Toast.LENGTH_LONG).show()
            }
        }

        moedaViewModel.atualizaMoedas()


    }

    private fun configuraRecyclerView() {
        rvMoedas = findViewById(R.id.rvMoedas)
        rvMoedas.layoutManager = LinearLayoutManager(this)
        rvMoedas.adapter = moedaAdapter
    }
}
