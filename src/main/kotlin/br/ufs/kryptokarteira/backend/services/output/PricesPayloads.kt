package br.ufs.kryptokarteira.backend.services.output

import br.ufs.kryptokarteira.backend.domain.Pricing

class PricesPayload(
        val label: String,
        val name: String,
        val buy: Float,
        val sell: Float
)

object PayloadForPrices {
    operator fun invoke(prices: List<Pricing>): List<PricesPayload> {
        return prices.map {
            PricesPayload(
                    label = it.currency.label.toUpperCase(),
                    name = it.currency.name,
                    sell = it.sellPrice,
                    buy = it.buyPrice
            )
        }
    }

}