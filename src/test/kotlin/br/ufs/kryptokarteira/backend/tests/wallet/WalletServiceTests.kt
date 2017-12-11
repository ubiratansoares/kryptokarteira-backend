package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.WalletService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class WalletServiceTests {

    lateinit var service: WalletService
    lateinit var banker: KryptoBanker
    lateinit var trader: CryptoCurrencyTrader

    val fakeAccount = BankAccount(
            owner = "Bira",
            savings = listOf(
                    Investiment(Currency.Real, 50000f),
                    Investiment(Currency.Brita, 0.0f),
                    Investiment(Currency.Bitcoin, 4.4f)
            )
    )

    @Before fun `before each test`() {
        banker = mock()
        trader = mock()
        service = WalletService(banker, trader)
    }

    @Test fun `should create new wallet with success`() {
        whenever(banker.newAccount()).thenReturn(fakeAccount)
        val operation = service.newWallet()
        assertThat(operation.statusCode).isEqualTo(200)
    }

    @Test fun `should retrieve existing wallet with success`() {
        whenever(banker.account(any())).thenReturn(fakeAccount)
        val operation = service.walletByOwner("Bira")
        assertThat(operation.statusCode).isEqualTo(200)
    }

}