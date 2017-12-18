package br.ufs.kryptokarteira.backend.services.output

import br.ufs.kryptokarteira.backend.domain.Currency
import com.google.gson.annotations.SerializedName

class HomePayload(
        val currencies : List<Currency>,
        val broking: List<SimulationPayload>,
        val wallet: WalletPayload
)

class SimulationPayload(
        val label: String,
        @SerializedName("buy_price") val buy: Float,
        @SerializedName("selling_price") val sell: Float
)
