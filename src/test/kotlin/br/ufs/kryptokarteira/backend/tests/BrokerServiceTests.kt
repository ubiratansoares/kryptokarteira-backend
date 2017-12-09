package br.ufs.kryptokarteira.backend.tests

import br.ufs.kryptokarteira.backend.domain.Currency.Bitcoin
import br.ufs.kryptokarteira.backend.domain.Currency.Brita
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.domain.Pricing
import br.ufs.kryptokarteira.backend.services.BrokerService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class BrokerServiceTests {

    lateinit var broker: PricesBroker
    lateinit var service: BrokerService

    @Before fun `before each test`() {
        broker = mock()
        service = BrokerService(broker)
    }

    @Test fun `should expose lastest prices successfully`() {
        whenever(broker.lastestPrices())
                .thenReturn(
                        listOf(
                                Pricing(Brita, 3.20f),
                                Pricing(Bitcoin, 48999f)
                        )
                )

        val operation = service.lastestPrices()

        assertThat(operation.statusCode).isEqualTo(200)
    }

}