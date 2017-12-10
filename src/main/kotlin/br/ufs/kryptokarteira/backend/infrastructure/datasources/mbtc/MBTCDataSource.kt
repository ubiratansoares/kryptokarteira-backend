package br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc

import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller

class MBTCDataSource(private val caller: RestCaller) {

    fun bitcoinPrice(): Float {
        val payload = caller.get(mercadoBitconAPI, MercadoBitconPayload::class)
        return payload.ticker.buyPrice.toFloat()
    }

    private companion object {
        val mercadoBitconAPI = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    }

}