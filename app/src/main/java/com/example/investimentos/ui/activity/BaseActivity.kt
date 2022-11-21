package com.example.investimentos.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.ViewModelProvider
import com.example.investimentos.R
import com.example.investimentos.databinding.ToolbarCambioBinding
import com.example.investimentos.repository.MoedaRepository
import com.example.investimentos.viewModel.MoedaViewModel
import com.example.investimentos.viewModel.MoedaViewModelFactory

open class BaseActivity : AppCompatActivity() {

    private val binding by lazy {
        ToolbarCambioBinding.inflate(layoutInflater)
    }

    protected lateinit var moedaViewModel: MoedaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        configuraToolbar()
    }

    private fun configuraToolbar() {
        setSupportActionBar(binding.toolbarPrincipal)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setIsHeading(binding.toolbarTitulo)
    }

    protected fun setIsHeading(textView: TextView) {
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