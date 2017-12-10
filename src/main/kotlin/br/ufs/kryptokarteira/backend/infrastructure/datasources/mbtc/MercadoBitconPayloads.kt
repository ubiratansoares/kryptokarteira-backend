package br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc

import com.google.gson.annotations.SerializedName

class MercadoBitconPayload(
        val ticker: TickerPayload
)

class TickerPayload(
        @SerializedName("buy") val sellPrice: String,
        @SerializedName("sell") val buyPrice: String
)