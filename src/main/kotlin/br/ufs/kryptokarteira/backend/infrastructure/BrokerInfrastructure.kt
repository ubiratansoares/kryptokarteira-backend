package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestIntegrationError
import com.google.common.cache.CacheBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit


class BrokerInfrastructure(
        private val bcb: BCBDataSource,
        private val mbtc: MBTCDataSource) : PricesBroker {

    private val cache by lazy {
        CacheBuilder.newBuilder()
                .expireAfterAccess(CACHE_EXPIRES_IN_MINUTES, TimeUnit.MINUTES)
                .build<String, List<Pricing>>()
    }

    override fun lastestPrices(): List<Pricing> {
        return cache.get("pricing", { fetchFromDataSources() })
    }

    private fun fetchFromDataSources(): List<Pricing> {

        try {
            return listOf(
                    Pricing(Currency.Brita, mbtc.bitcoinPrice()),
                    Pricing(Currency.Bitcoin, bcb.britaPricing())
            )
        } catch (unhandled: Throwable) {
            throw asDomainError(unhandled)
        }
    }

    private fun asDomainError(unmapped: Throwable): DomainError = when (unmapped) {
        is SocketTimeoutException -> ExternalServiceTimeout()
        is UnknownHostException -> ExternalServiceUnavailable()
        is RestIntegrationError.InternalServerErrorRest -> ExternalServiceUnavailable()
        is RestIntegrationError.ClientErrorRest -> ExternalServiceIntegrationError()
        is MalformedJsonException -> ExternalServiceContractError()
        is JsonIOException -> ExternalServiceContractError()
        is JsonParseException -> ExternalServiceContractError()
        is JsonSyntaxException -> ExternalServiceContractError()
        else -> UnknownInternalError()
    }

    private companion object {
        val CACHE_EXPIRES_IN_MINUTES = 5L
    }

}