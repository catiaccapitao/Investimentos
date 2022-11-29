package com.example.investimentos.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MoedaModel(
    @SerializedName("name")
    val nomeMoeda: String? = null,
    @SerializedName("variation")
    val variacaoMoeda: Double? = null,
    @SerializedName("buy")
    var valorCompra: Double? = null,
    @SerializedName("sell")
    var valorVenda: Double? = null,
    var isoMoeda: String = ""
) : Serializable