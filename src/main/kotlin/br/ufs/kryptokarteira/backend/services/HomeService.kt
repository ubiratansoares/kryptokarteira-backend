package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.CryptoBanker
import br.ufs.kryptokarteira.backend.domain.Currency.*
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.domain.Pricing
import br.ufs.kryptokarteira.backend.services.output.HomePayload
import br.ufs.kryptokarteira.backend.services.output.SimulationPayload
import br.ufs.kryptokarteira.backend.services.output.WalletPayloadFromBankAccount
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation

class HomeService(
        private val broker: PricesBroker,
        private val banker: CryptoBanker) {

    fun dataForHomeScreen(owner: String): ServiceOperation {
        val prices = broker.lastestPrices()
        val account = banker.account(owner)
        val payload = HomePayload(
                currencies = listOf(Bitcoin, Brita, Real),
                wallet = WalletPayloadFromBankAccount(account),
                broking = SimulationPayloadFromBroking(prices)
        )

        return ServiceOperation(200, JsonSerializer.asJson(payload))
    }
}

object SimulationPayloadFromBroking {
    operator fun invoke(prices: List<Pricing>): List<SimulationPayload> {
        return prices.map {
            SimulationPayload(
                    label = it.currency.label,
                    sell = it.sellPrice,
                    buy = it.buyPrice
            )
        }
    }
}