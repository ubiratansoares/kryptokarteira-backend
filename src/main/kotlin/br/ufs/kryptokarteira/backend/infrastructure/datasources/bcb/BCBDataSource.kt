package br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb

import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller
import br.ufs.kryptokarteira.backend.infrastructure.util.PricingValues

class BCBDataSource(private val caller: RestCaller) {

    fun britaPrices(formattedDate : String): PricingValues {

        val fromYesterday = bcbAPI
                .replace(datePlaceholder, formattedDate)
                .replace(currencyPlaceholder, "USD")

        val bcbPayload = caller.get(fromYesterday, BCBPayload::class)
        val dollarPricing = bcbPayload.value.last()
        return PricingValues(
                buy = dollarPricing.buyPrice,
                sell = dollarPricing.sellPrice
        )
    }

    private companion object {
        val datePlaceholder = "<dd-mm-yyyy>"
        val currencyPlaceholder = "<CURRENCY>"

        val bcbAPI = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
                "CotacaoMoedaDia(moeda=%27$currencyPlaceholder%27,dataCotacao=%27$datePlaceholder%27)" +
                "?format=json"
    }
}