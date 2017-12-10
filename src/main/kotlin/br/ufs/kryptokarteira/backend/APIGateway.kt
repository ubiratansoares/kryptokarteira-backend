package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.BrokerService
import spark.kotlin.*

class APIGateway(private val brokerService: BrokerService) {

    fun start() {

        before {
            val authorized = CheckAuthorization(request)
            if (!authorized) {
                deny(401, "Invalid or missing API credentials")
            }
        }

        get(path = "api/v1/broker") {
            val pricing = brokerService.lastestPrices()
            type(contentType = "application/json")
            status(pricing.statusCode)
            pricing.result
        }

        notFound {
            reply(
                    statusCode = 404,
                    message = "Not found"
            )
        }

        internalServerError {
            reply(
                    statusCode = 500,
                    message = "Its not you ... Its us #sadpanda"
            )
        }

        exception(DomainError::class, {
            when (exception) {

                is ExternalServiceUnavailable ->
                    replyWithError(
                            statusCode = 502,
                            errorMessage = "Some internal system is unavailable"
                    )

                is ExternalServiceTimeout ->
                    replyWithError(
                            statusCode = 504,
                            errorMessage = "Received a timeout from an internal system"
                    )

                is ExternalServiceIntegrationError ->
                    replyWithError(
                            statusCode = 503,
                            errorMessage = "Integration error with an internal system"
                    )

                is ExternalServiceContractError ->
                    replyWithError(
                            statusCode = 500,
                            errorMessage = "Contract with an internal system is broken"
                    )

                else -> {
                    replyWithError(
                            statusCode = 500,
                            errorMessage = "Its not you ... Its us #sadpanda"
                    )
                }
            }
        })
    }

}