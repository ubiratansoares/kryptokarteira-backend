package br.ufs.kryptokarteira.backend.tests.account

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.infrastructure.AccountInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.AccountPayload
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.RestDBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.SavingPayload
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestIntegrationError
import com.google.gson.JsonSyntaxException
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException


class AccountInfrastructureTests {

    lateinit var infrastructure: AccountInfrastructure
    lateinit var dataSource: RestDBDataSource

    val fakeInitalSavings = listOf(
            SavingPayload("btc", 0f),
            SavingPayload("bta", 0f),
            SavingPayload("blr", 100000f)
    )

    val fakeIncomingSavings = listOf(
            SavingPayload("btc", 2f),
            SavingPayload("bta", 456f),
            SavingPayload("blr", 11000f)
    )

    val giveway = listOf(
            Investiment(Currency.Bitcoin, 0f),
            Investiment(Currency.Brita, 0f),
            Investiment(Currency.Real, 100000f)
    )

    val rawAccount = AccountPayload("Bira", fakeInitalSavings)

    @Before fun `before each test`() {
        dataSource = mock()
        infrastructure = AccountInfrastructure(dataSource)
    }

    @Test fun `should create account with success`() {
        val captor: KArgumentCaptor<List<Investiment>> = argumentCaptor()

        whenever(dataSource.createAccount(any())).thenReturn(rawAccount)
        infrastructure.createAccount(giveway)

        verify(dataSource, times(1)).createAccount(captor.capture())
        assertThat(captor.firstValue).isEqualTo(giveway)
        verifyNoMoreInteractions(dataSource)
    }

    @Test fun `should retrieve account with success`() {
        val captor: KArgumentCaptor<String> = argumentCaptor()

        whenever(dataSource.retrieveAccount(any())).thenReturn(rawAccount)
        infrastructure.accountForOwner("Jacqueline")

        verify(dataSource, times(1)).retrieveAccount(captor.capture())
        verifyNoMoreInteractions(dataSource)

        assertThat(captor.firstValue).isEqualTo("Jacqueline")
    }

    @Test fun `should update savings with success`() {
        val wallet: KArgumentCaptor<String> = argumentCaptor()
        val newSavings: KArgumentCaptor<List<Investiment>> = argumentCaptor()

        whenever(dataSource.updateSavings(any(), any())).then { Unit }

        infrastructure.updateSavings("Bira", giveway)

        verify(dataSource, times(1))
                .updateSavings(wallet.capture(), newSavings.capture())

        assertThat(wallet.firstValue).isEqualTo("Bira")
        assertThat(newSavings.firstValue).isEqualTo(giveway)
        verifyNoMoreInteractions(dataSource)
    }

    @Test fun `should translate account creation error as domain error`() {

        whenever(dataSource.createAccount(any()))
                .thenAnswer {
                    throw RestIntegrationError.ClientError
                }

        assertThatThrownBy { infrastructure.createAccount(giveway) }
                .isInstanceOf(ExternalServiceIntegrationError::class.java)

        verify(dataSource, times(1)).createAccount(any())
        verifyNoMoreInteractions(dataSource)
    }

    @Test fun `should translate account retrieval error as domain error`() {

        whenever(dataSource.retrieveAccount(any()))
                .thenAnswer {
                    throw SocketTimeoutException()
                }

        assertThatThrownBy { infrastructure.accountForOwner("Bira") }
                .isInstanceOf(ExternalServiceTimeout::class.java)

        verify(dataSource, times(1)).retrieveAccount("Bira")
        verifyNoMoreInteractions(dataSource)
    }

    @Test fun `should translate savings update error as domain error`() {

        whenever(dataSource.updateSavings(any(), any()))
                .thenThrow(JsonSyntaxException("Cannot have comments!"))


        assertThatThrownBy { infrastructure.updateSavings("Bira", giveway) }
                .isInstanceOf(ExternalServiceContractError::class.java)

        verify(dataSource, times(1))
                .updateSavings("Bira", giveway)

        verifyNoMoreInteractions(dataSource)
    }
}