package com.example.investimentos.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.adapter.MoedaAdapter
import com.example.investimentos.databinding.ActivityHomeBinding
import com.example.investimentos.databinding.ToolbarBinding
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MainViewModelFactory
import com.example.investimentos.viewModel.MoedaViewModel

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }
    private val bindingToolbar by lazy {
        ToolbarBinding.inflate(layoutInflater)
    }
    private val moedaAdapter by lazy {
        MoedaAdapter()
    }

    lateinit var moedaViewModel: MoedaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        moedaViewModel = ViewModelProvider(this, MainViewModelFactory(MoedaRepository())).get(
            MoedaViewModel::class.java
        )
        configuraRecyclerView()
        sincronizaMoedas()
        setSupportActionBar(bindingToolbar.toolbar)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
//            it.setDisplayHomeAsUpEnabled(true)
        }
        bindingToolbar.toolbarTitle.let { titulo ->
            titulo.contentDescription = "Lista de ${titulo.text}"
        }
//        setIsHeading(bindingToolbar.toolbarTitle)
    }

    private fun sincronizaMoedas() {
        moedaViewModel.listaDeMoedas.observe(this) {
            moedaAdapter.refresh(it)
        }
        moedaViewModel.atualizaMoedas()
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

    private fun setIsHeading(textView: TextView) {
        //alterar eventos de acessibilidade
        //uso para texto como titulo:
        ViewCompat.setAccessibilityDelegate(textView, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfoCompat
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    host.isAccessibilityHeading = true
                } else {
                    info.isHeading = true
                }
            }
        })
    }
}
