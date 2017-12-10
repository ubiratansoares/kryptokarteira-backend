package br.ufs.kryptokarteira.backend.infrastructure.networking

sealed class RestIntegrationError : Throwable() {
    object ClientErrorRest : RestIntegrationError()
    object InternalServerErrorRest : RestIntegrationError()
}