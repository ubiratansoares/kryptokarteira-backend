package br.ufs.kryptokarteira.backend.rest

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.*
import spark.kotlin.*

class APIGateway(
        private val brokerService: BrokerService,
        private val walletService: WalletService) {

    fun start() {

        config {
            port = 8080

            staticFiles {
                location = "/public"
            }
        }

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

        get(path = "api/v1/wallet/new") {
            val wallet = walletService.newWallet()
            reply {
                wallet.statusCode withMessage wallet.result
            }
        }

        get(path = "api/v1/wallet/:owner/details") {

            val op = request.params(":owner")
            val wallet = walletService.walletByOwner(op)
            reply {
                wallet.statusCode withMessage wallet.result
            }
        }

        post(path = "api/v1/wallet/:owner/transaction") {
            val owner = request.params(":owner")
            val body = request.body()
            val transaction = walletService.newTransaction(owner, body)

            reply {
                transaction.statusCode withMessage transaction.result
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

                is CannotPerformTransaction -> failure {
                    409 withMessage "You are not able to perform this transaction"
                }

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

        exception(ValidationError::class, {
            when (exception) {

                is MissingTransactionBody -> failure {
                    400 withMessage "Body is mandatory for this request"
                }

                is CannotDeserializeJson -> failure {
                    400 withMessage "Body does not match desired contract"
                }

                is MissingTransactionType -> failure {
                    400 withMessage "Body field *type* is mandatory for this request"
                }

                is InvalidTransactionType -> failure {
                    400 withMessage "Only types (sell|buy) accepted for field *transaction*"
                }

                is MissingCurrency -> failure {
                    400 withMessage "Body field *currency* is mandatory for this request"
                }

                is InvalidCurrency -> failure {
                    400 withMessage "Only types (btc|bta) accepted for field *currency*"
                }

                is MissingAmount -> failure {
                    400 withMessage "Body field *amount* is mandatory for this request"
                }

                is InvalidAmount -> failure {
                    400 withMessage "Only positive amounts allowed for transactions"
                }

                else -> failure {
                    500 withMessage "Its not you ... Its us #sadpanda"
                }
            }
        })
    }

}