package br.ufs.kryptokarteira.backend.tests.broker

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.infrastructure.BrokerInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestIntegrationError
import br.ufs.kryptokarteira.backend.infrastructure.util.PricingValues
import com.google.gson.JsonSyntaxException
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class BrokerInfrastructureTests {

    lateinit var bcb: BCBDataSource
    lateinit var mbtc: MBTCDataSource
    lateinit var infrastructure: BrokerInfrastructure


    val brita = PricingValues(3.2886f, 3.2892f)
    val bitcon = PricingValues(52600f, 52730f)

    @Before fun `before each test`() {
        bcb = mock()
        mbtc = mock()
        infrastructure = BrokerInfrastructure(bcb, mbtc)
    }

    @Test fun `should return prices at first time when both data sources available`() {


        `data sources will return with success`()

        val prices = infrastructure.lastestPrices()

        assertThat(prices).isNotNull.isNotEmpty

        prices.forEach {
            when (it.currency) {
                Currency.Bitcoin -> `check pricing values applied`(it, bitcon)
                Currency.Brita -> `check pricing values applied`(it, brita)
            }
        }
    }

    @Test fun `should return prices from cache when available`() {

        `data sources will return with success`()

        for (i in 1..10) infrastructure.lastestPrices()
        verify(bcb, times(1)).britaPrices(any())
        verify(mbtc, times(1)).bitcoinPrices()
        verifyNoMoreInteractions(bcb)
        verifyNoMoreInteractions(mbtc)
    }

    @Test fun `should throw service unavailable error when some data source is unreachable`() {
        whenever(mbtc.bitcoinPrices())
                .thenAnswer {
                    throw UnknownHostException()
                }

        assertThatThrownBy { infrastructure.lastestPrices() }
                .isInstanceOf(ExternalServiceUnavailable::class.java)
    }

    @Test fun `should throw timeout error when some data source timed out`() {
        whenever(mbtc.bitcoinPrices())
                .thenAnswer {
                    throw SocketTimeoutException("Timeout")
                }

        assertThatThrownBy { infrastructure.lastestPrices() }
                .isInstanceOf(ExternalServiceTimeout::class.java)
    }

    @Test fun `should throw integration error when some data source replies with rest error`() {
        whenever(mbtc.bitcoinPrices())
                .thenAnswer {
                    throw RestIntegrationError.ClientError
                }

        assertThatThrownBy { infrastructure.lastestPrices() }
                .isInstanceOf(ExternalServiceIntegrationError::class.java)
    }

    @Test fun `should throw contract error when some data source replies with wrong json`() {
        whenever(mbtc.bitcoinPrices())
                .thenAnswer {
                    throw JsonSyntaxException("JSON dont support comments")
                }

        assertThatThrownBy { infrastructure.lastestPrices() }
                .isInstanceOf(ExternalServiceContractError::class.java)
    }

    @Test fun `should throw internal error when unmapped things happen`() {
        whenever(mbtc.bitcoinPrices())
                .thenAnswer {
                    throw IllegalArgumentException("WTF")
                }

        assertThatThrownBy { infrastructure.lastestPrices() }
                .isInstanceOf(UnknownInternalError::class.java)
    }

    private fun `check pricing values applied`(target: Pricing, origin: PricingValues) {
        assertThat(target.sellPrice).isEqualTo(origin.sell)
        assertThat(target.buyPrice).isEqualTo(origin.buy)
    }

    private fun `data sources will return with success`() {
        whenever(bcb.britaPrices(any())).thenReturn(brita)
        whenever(mbtc.bitcoinPrices()).thenReturn(bitcon)
    }


}