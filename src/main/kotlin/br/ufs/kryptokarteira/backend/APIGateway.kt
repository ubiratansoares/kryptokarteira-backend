package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.services.PricingService
import spark.kotlin.get
import spark.kotlin.internalServerError
import spark.kotlin.notFound

class APIGateway(private val pricingService: PricingService) {

    fun start() {

        get(path = "/pricing") {
            val pricing = pricingService.lastestPrices()
            type(contentType = "application/json")
            status(pricing.statusCode)
            pricing.result
        }

        notFound {
            type(contentType = "application/json")
            "{\"message\":\"Not Found\"}"
        }

        internalServerError {
            type(contentType = "application/json")
            "{\"message\":\"It`s not you. It`s us ... :sadpanda: \"}"
        }
    }

}