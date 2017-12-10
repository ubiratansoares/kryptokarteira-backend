package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.rest.APIGateway

fun main(args: Array<String>) {
    val gateway: APIGateway = Injector.gateway
    gateway.start()
}