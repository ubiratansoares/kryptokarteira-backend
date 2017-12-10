package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.Currency
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.domain.Pricing
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import br.ufs.kryptokarteira.backend.infrastructure.util.AsDomainError
import com.google.common.cache.CacheBuilder
import java.util.concurrent.ExecutionException
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
        try {
            return cache.get("pricing", { fetchFromDataSources() })
        } catch (e: ExecutionException) {
            throw e.cause!! // Thanks, Guava :(
        }
    }

    private fun fetchFromDataSources(): List<Pricing> {

        try {
            val bitcoinValues = mbtc.bitcoinPrices()
            val britaValues = bcb.britaPrices()

            return listOf(
                    Pricing(Currency.Brita, britaValues.buy, britaValues.sell),
                    Pricing(Currency.Bitcoin, bitcoinValues.buy, bitcoinValues.sell)
            )
        } catch (error: Throwable) {
            throw AsDomainError(error)
        }
    }

    private companion object {
        val CACHE_EXPIRES_IN_MINUTES = 5L
    }

}