package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.Currency
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.domain.Pricing
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import com.google.common.cache.CacheBuilder
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
        } catch (e: Throwable) {
            throw InternalError()
        }

    }

    private companion object {
        val CACHE_EXPIRES_IN_MINUTES = 5L
    }

}