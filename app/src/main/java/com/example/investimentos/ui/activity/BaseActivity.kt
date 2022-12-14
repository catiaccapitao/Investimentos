package com.example.investimentos.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.MOEDAS
import com.example.investimentos.R
import com.example.investimentos.databinding.ToolbarBinding
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import com.example.investimentos.viewModel.MoedaViewModelFactory

open class BaseActivity : AppCompatActivity() {

    private val binding by lazy {
        ToolbarBinding.inflate(layoutInflater)
    }

    protected lateinit var moedaViewModel: MoedaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    protected open fun configuraToolbar(tvTitulo: TextView, btnVoltar: ImageButton, titulo: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setIsHeading(tvTitulo)
        tvTitulo.text = titulo
        if (titulo == MOEDAS) {
            btnVoltar.visibility = View.GONE
        } else {
            btnVoltar.visibility = View.VISIBLE
            btnVoltar.setOnClickListener { finish() }
        }
    }

    private fun setIsHeading(textView: TextView) {
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

    protected fun inicializaViewModel() {
        moedaViewModel =
            ViewModelProvider(
                this,
                MoedaViewModelFactory(MoedaRepository())
            )[MoedaViewModel::class.java]
    }
}