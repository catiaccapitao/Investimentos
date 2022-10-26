package com.example.investimentos.model

import com.google.gson.annotations.SerializedName

data class MoedaModel (
    @SerializedName("name")
    val nomeMoeda: String? = null,
    @SerializedName("variation")
    val variacaoMoeda: Double? = null,
    @SerializedName("buy")
    val valorCompra: Double? = null,
    @SerializedName("sell")
    val valorVenda: Double? = null
)