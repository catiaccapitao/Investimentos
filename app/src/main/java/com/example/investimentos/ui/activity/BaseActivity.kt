package com.example.investimentos.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.example.investimentos.R
import com.example.investimentos.databinding.ToolbarCambioBinding

open class BaseActivity : AppCompatActivity() {

    val binding by lazy {
        ToolbarCambioBinding.inflate(layoutInflater)
    }

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
}