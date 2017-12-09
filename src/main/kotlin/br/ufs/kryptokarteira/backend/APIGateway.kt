package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.BrokerService
import spark.kotlin.exception
import spark.kotlin.get
import spark.kotlin.internalServerError
import spark.kotlin.notFound

class APIGateway(private val brokerService: BrokerService) {

    fun start() {

        get(path = "/broker") {
            val pricing = brokerService.lastestPrices()
            type(contentType = "application/json")
            status(pricing.statusCode)
            pricing.result
        }

        notFound {
            reply(message = "Not found")
        }

        internalServerError {
            reply(message = "Its not you ... Its us #sadpanda")
        }

        exception(DomainError::class, {
            when (exception) {

                is ExternalServiceUnavailable ->
                    replyWith(
                            statusCode = 502,
                            errorMessage = "Some internal system is unavailable"
                    )

                is ExternalServiceTimeout ->
                    replyWith(
                            statusCode = 504,
                            errorMessage = "Received a timeout from an internal system"
                    )

                is ExternalServiceIntegrationError ->
                    replyWith(
                            statusCode = 503,
                            errorMessage = "Integration error with an internal system"
                    )

                is ExternalServiceContractError ->
                    replyWith(
                            statusCode = 500,
                            errorMessage = "Contract with an internal system is broken"
                    )

                else -> {
                    replyWith(
                            statusCode = 500,
                            errorMessage = "Its not you ... Its us #sadpanda"
                    )
                }
            }
        })
    }

}