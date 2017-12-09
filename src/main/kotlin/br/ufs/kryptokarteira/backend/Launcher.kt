package br.ufs.kryptokarteira.backend

fun main(args: Array<String>) {
    val gateway: APIGateway = Injector.gateway
    gateway.start()
}