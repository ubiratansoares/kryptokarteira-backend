package br.ufs.kryptokarteira.backend.services.output

import com.google.gson.annotations.SerializedName

class HomePayload(
        val broking: List<SimulationPayload>,
        val wallet: WalletPayload
)

class SimulationPayload(
        val label: String,
        val name: String,
        @SerializedName("buy_price") val buy: Float,
        @SerializedName("selling_price") val sell: Float,
        @SerializedName("earns_if_selling") val earns: Float
)
