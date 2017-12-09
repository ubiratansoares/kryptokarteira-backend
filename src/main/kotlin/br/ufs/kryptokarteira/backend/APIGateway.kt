package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.BrokerService
import spark.kotlin.*

class APIGateway(private val brokerService: BrokerService) {

    fun start() {

        get(path = "/pricing") {
            val pricing = brokerService.lastestPrices()
            type(contentType = "application/json")
            status(pricing.statusCode)
            pricing.result
        }

        notFound {
            defaultRoutingWith(message = "Not found")
        }

        internalServerError {
            defaultRoutingWith(message = "Its not you ... Its us #sadpanda")
        }

        exception(DomainError::class, {
            when (exception) {

                is ExternalServiceUnavailable ->
                    failWith(
                            statusCode = 502,
                            errorMessage = "Some internal system is unavailable"
                    )

                is ExternalServiceTimeout ->
                    failWith(
                            statusCode = 504,
                            errorMessage = "Received a timeout from an internal system"
                    )

                is ExternalServiceIntegrationError ->
                    failWith(
                            statusCode = 503,
                            errorMessage = "Integration error with an internal system"
                    )

                is ExternalServiceContractError ->
                    failWith(
                            statusCode = 500,
                            errorMessage = "Contract with an internal system is broken"
                    )

                else -> {
                    failWith(
                            statusCode = 500,
                            errorMessage = "Its not you ... Its us #sadpanda"
                    )
                }
            }
        })
    }

    private fun RouteHandler.defaultRoutingWith(message: String): String {
        type(contentType = "application/json")
        return "{\"message\":\"$message\"}"
    }

    private fun ExceptionHandler.failWith(statusCode: Int, errorMessage: String) {
        status(statusCode)
        type(contentType = "application/json")
        response.body("{\"message\":\"$errorMessage\"}")
    }

}