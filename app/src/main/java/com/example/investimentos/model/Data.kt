package com.example.investimentos.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("currencies")
    var currencies: ResultData = ResultData()
)

data class ResultData(
    @SerializedName("source")
    var source: String = "",
    @SerializedName("USD")
    var USD: MoedaModel? = null,
    @SerializedName("EUR")
    var EUR: MoedaModel? = null,
    @SerializedName("GBP")
    var GBP: MoedaModel? = null,
    @SerializedName("ARS")
    var ARS: MoedaModel? = null,
    @SerializedName("CAD")
    var CAD: MoedaModel? = null
)