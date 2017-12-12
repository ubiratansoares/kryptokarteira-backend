package br.ufs.kryptokarteira.backend.infrastructure.util

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestIntegrationError
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object AsDomainError {

    operator fun invoke(unmapped: Throwable): DomainError = when (unmapped) {
        is SocketTimeoutException -> ExternalServiceTimeout()
        is UnknownHostException -> ExternalServiceUnavailable()
        is RestIntegrationError.InternalServerError -> ExternalServiceUnavailable()
        is RestIntegrationError.ClientError -> ExternalServiceIntegrationError()
        is MalformedJsonException -> ExternalServiceContractError()
        is JsonIOException -> ExternalServiceContractError()
        is JsonParseException -> ExternalServiceContractError()
        is JsonSyntaxException -> ExternalServiceContractError()
        else -> UnknownInternalError()
    }
}