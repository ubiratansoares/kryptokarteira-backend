package br.ufs.kryptokarteira.backend.rest

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.BrokerService
import spark.kotlin.*

class APIGateway(private val brokerService: BrokerService) {

    fun start() {

        before {
            val authorized = CheckAuthorization(request)
            if (!authorized) {
                deny { 401 withMessage "Invalid or missing API credentials" }
            }
        }

        get(path = "api/v1/broker") {
            val pricing = brokerService.lastestPrices()
            reply {
                pricing.statusCode withMessage pricing.result
            }
        }

        notFound {
            failure {
                404 withMessage "Not found"
            }
        }

        internalServerError {
            failure {
                500 withMessage "Its not you ... Its us #sadpanda"
            }
        }

        exception(DomainError::class, {
            when (exception) {

                is ExternalServiceUnavailable -> failure {
                    502 withMessage "Some internal system is unavailable"
                }

                is ExternalServiceTimeout -> failure {
                    504 withMessage "Received a timeout from an internal system"
                }

                is ExternalServiceIntegrationError -> failure {
                    503 withMessage "Integration error with an internal system"
                }

                is ExternalServiceContractError -> failure {
                    500 withMessage "Contract with an internal system is broken"
                }

                else -> failure {
                    500 withMessage "Its not you ... Its us #sadpanda"
                }
            }
        })
    }

}