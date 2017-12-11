package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.services.output.PricesPayload
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation

class BrokerService(private val broker: PricesBroker) {

    private val mapper = JsonSerializer

    fun lastestPrices(): ServiceOperation {

        val pricing = broker.lastestPrices()

        val output = pricing.map {
            PricesPayload(
                    label = it.currency.label.toUpperCase(),
                    name = it.currency.name,
                    sell = it.sellPrice,
                    buy = it.buyPrice
            )
        }

        return ServiceOperation(200, mapper.asJson(output))
    }

}