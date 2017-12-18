package br.ufs.kryptokarteira.backend.services.output

import br.ufs.kryptokarteira.backend.domain.Pricing

class PricesPayload(
        val label: String,
        val buy: Float,
        val sell: Float
)

object PayloadForPrices {
    operator fun invoke(prices: List<Pricing>): List<PricesPayload> {
        return prices.map { PayloadForPrice(it) }
    }
}

object PayloadForPrice {
    operator fun invoke(pricing: Pricing): PricesPayload {
        return with(pricing) {
            PricesPayload(
                    label = currency.label,
                    sell = sellPrice,
                    buy = buyPrice
            )
        }
    }
}