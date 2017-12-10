package br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc

import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller
import br.ufs.kryptokarteira.backend.infrastructure.util.PricingValues

class MBTCDataSource(private val caller: RestCaller) {

    fun bitcoinPrices(): PricingValues {
        val payload = caller.get(mercadoBitconAPI, MercadoBitconPayload::class)

        return PricingValues(
                buy = payload.ticker.buyPrice.toFloat(),
                sell = payload.ticker.sellPrice.toFloat()
        )
    }

    private companion object {
        val mercadoBitconAPI = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    }

}