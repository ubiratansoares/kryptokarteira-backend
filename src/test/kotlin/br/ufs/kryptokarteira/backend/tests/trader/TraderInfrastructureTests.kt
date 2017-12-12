package br.ufs.kryptokarteira.backend.tests.trader

import br.ufs.kryptokarteira.backend.domain.Currency
import br.ufs.kryptokarteira.backend.domain.DataForTransaction
import br.ufs.kryptokarteira.backend.domain.ExternalServiceTimeout
import br.ufs.kryptokarteira.backend.infrastructure.TraderInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.RestDBDataSource
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException

class TraderInfrastructureTests {

    lateinit var infrastructure: TraderInfrastructure
    lateinit var dataSource: RestDBDataSource
    lateinit var captor: KArgumentCaptor<DataForTransaction>
    val data = DataForTransaction(
            owner = "Bira",
            type = "buy",
            currency = Currency.Bitcoin,
            amount = 10f
    )

    @Before fun `before each test`() {
        dataSource = mock()
        infrastructure = TraderInfrastructure(dataSource)
        captor = argumentCaptor()
    }

    @Test fun `should perform transaction with success`() {

        `transaction at data source leve will succeed`()

        infrastructure.performTransaction(data)

        `verify data source called one single time for new transaction`()

        assertThat(captor.firstValue).isEqualTo(data)
    }

    @Test fun `should translate error when performing transaction to domain`() {
        val error = SocketTimeoutException("Timed out")
        `transaction at data source level will fail with`(error)

        assertThatThrownBy { infrastructure.performTransaction(data) }
                .isInstanceOf(ExternalServiceTimeout::class.java)

        `verify data source called one single time for new transaction`()
    }

    private fun `transaction at data source leve will succeed`() {
        whenever(dataSource.registerTransaction(any(), any())).then { Unit }
    }

    private fun `transaction at data source level will fail with`(error: Throwable) {
        whenever(dataSource.registerTransaction(any(), any()))
                .thenAnswer { throw error }
    }

    private fun `verify data source called one single time for new transaction`() {
        verify(dataSource, times(1)).registerTransaction(captor.capture(), any())
        verifyNoMoreInteractions(dataSource)
    }
}