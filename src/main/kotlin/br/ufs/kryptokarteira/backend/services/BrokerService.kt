package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.services.output.PricePayload
import br.ufs.kryptokarteira.backend.services.util.OutputMapper
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation

class BrokerService(private val broker: PricesBroker) {

    private val mapper = OutputMapper

    fun lastestPrices(): ServiceOperation {

        val pricing = broker.lastestPrices()

        val output = pricing.map {
            PricePayload(
                    label = it.currency.label.toUpperCase(),
                    name = it.currency.name,
                    sell = it.sellPrice,
                    buy = it.buyPrice
            )
        }

        return ServiceOperation(200, mapper.asJson(output))
    }

}