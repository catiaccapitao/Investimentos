package com.example.investimentos

import android.os.Bundle
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
            moedaAdapter.refresh(it)
        }

        moedaViewModel.atualizaMoedas()

    }

    private fun configuraRecyclerView() {
        rvMoedas = findViewById(R.id.rvMoedas)
        rvMoedas.adapter = moedaAdapter
//            MoedaAdapter(onClick = {
//            val cambio = Intent(this, CambioActivity::class.java)
//            startActivity(cambio)
//        })
        rvMoedas.layoutManager = LinearLayoutManager(this)
    }
}
