package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.Currency
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.domain.Pricing
import br.ufs.kryptokarteira.backend.infrastructure.inputs.BCBPayload
import br.ufs.kryptokarteira.backend.infrastructure.inputs.MercadoBitconPayload
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BrokerInfrastructure(
        private val caller: RestCaller) : PricesBroker {

    private val datePlaceholder = "<dd-mm-yyyy>"
    private val currencyPlaceholder = "<CURRENCY>"

    private val mercadoBitconAPI = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    private val bcbAPI = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/" +
            "CotacaoMoedaDia(moeda=%27$currencyPlaceholder%27,dataCotacao=%27$datePlaceholder%27)" +
            "?format=json"

    override fun lastestPrices(): List<Pricing> {

        val dateTime = LocalDateTime.now()
        val formattedDate = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        val fromYesterday = bcbAPI
                .replace(datePlaceholder, formattedDate)
                .replace(currencyPlaceholder, "USD")

        val bcbPayload = caller.get(fromYesterday, BCBPayload::class)
        val dollarPricing = bcbPayload.value.last()

        val mercadoBitcoinPayload = caller.get(mercadoBitconAPI, MercadoBitconPayload::class)
        val bitcoingPricing = mercadoBitcoinPayload.ticker

        return listOf(
                Pricing(Currency.Brita, dollarPricing.sellPrice),
                Pricing(Currency.Bitcoin, bitcoingPricing.sellPrice.toFloat())
        )
    }
}