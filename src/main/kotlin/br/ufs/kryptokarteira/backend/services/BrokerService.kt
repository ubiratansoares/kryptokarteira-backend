package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.services.output.PayloadForPrices
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation

class BrokerService(private val broker: PricesBroker) {

    private val mapper = JsonSerializer

    fun lastestPrices(): ServiceOperation {
        val prices = broker.lastestPrices()
        val output = PayloadForPrices(prices)
        return ServiceOperation(200, mapper.asJson(output))
    }

}