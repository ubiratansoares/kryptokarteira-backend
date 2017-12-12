package br.ufs.kryptokarteira.backend.infrastructure.networking

sealed class RestIntegrationError : Throwable() {
    object ClientError : RestIntegrationError()
    object InternalServerError : RestIntegrationError()
}