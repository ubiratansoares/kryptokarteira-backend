package br.ufs.kryptokarteira.backend.infrastructure.inputs

import com.google.gson.annotations.SerializedName

class BCBPayload(val value: List<DollarPricingPayload>)

class DollarPricingPayload(
        @SerializedName("cotacaoCompra") val sellPrice: Float,
        @SerializedName("cotacaoVenda") val buyPrice: Float
)