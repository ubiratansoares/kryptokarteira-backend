package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.domain.Currency.*
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import java.util.*

class WalletTests {

    lateinit var trader: CryptoCurrencyTrader
    lateinit var broker: PricesBroker
    lateinit var banker: CryptoBanker
    lateinit var wallet: Wallet

    @Before fun `before each test`() {
        trader = mock()
        broker = mock()
        banker = mock()
        val owner = UUID.randomUUID().toString()

        setupRoles()

        wallet = Wallet(owner, banker, trader, broker)
    }

    @Test fun `can buy criptocurrency with success when money available`() {

        `transaction will succeed`()

        assertValidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Bitcoin, amount = 2f)
        }

        `verify trader interactions for single transaction`()
        `verify banker actioned for savings update`()
    }

    @Test fun `can sell criptocurrency with success`() {

        `transaction will succeed`()

        assertValidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Bitcoin, amount = 2f)
        }

        `verify trader interactions for single transaction`()
        `verify banker actioned for savings update`()
    }

    @Test fun `can not buy criptocurrency with success when doesnt have money enough`() {

        assertInvalidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Bitcoin, amount = 20f)
        }

        `verify banker actioned only for account retrieval`()
        `trader not actioned`()
    }

    @Test fun `cannot buy criptocurrency with negative amounts`() {

        assertInvalidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Bitcoin, amount = -10f)
        }

        `verify banker actioned only for account retrieval`()
        `trader not actioned`()
    }

    @Test fun `cannot buy criptocurrency with zero amount`() {

        assertInvalidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Brita, amount = 0.0f)
        }

        `verify banker actioned only for account retrieval`()
        `trader not actioned`()
    }

    @Test fun `cannot sell criptocurrency with negative amounts`() {

        assertInvalidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Bitcoin, amount = -100f)
        }

        `verify banker actioned only for account retrieval`()
        `trader not actioned`()
    }

    @Test fun `cannot sell criptocurrency with zero amounts`() {

        assertInvalidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Brita, amount = 0.0f)
        }

        `verify banker actioned only for account retrieval`()
        `trader not actioned`()
    }

    @Test fun `can only sell criptocurrency with success when amount is on wallet`() {

        assertInvalidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Brita, amount = 20.0f)
        }

        `verify banker actioned only for account retrieval`()
        `trader not actioned`()
    }

    private fun setupRoles() {
        whenever(broker.lastestPrices())
                .thenReturn(
                        listOf(
                                Pricing(Bitcoin, buyPrice = 10f, sellPrice = 10f),
                                Pricing(Brita, buyPrice = 2f, sellPrice = 2f)
                        )
                )

        val savings = listOf(
                Investiment(Bitcoin, 5f),
                Investiment(Brita, 10f),
                Investiment(Real, 100f)
        )


        val account = BankAccount("Bira", savings)
        whenever(banker.account(any())).thenReturn(account)
    }

    private fun `trader not actioned`() {
        verifyZeroInteractions(trader)
    }

    private fun `transaction will succeed`() {
        whenever(trader.performTransaction(any())).thenReturn(Transaction("Success!"))
    }

    private fun assertInvalidTransaction(func: () -> Transaction) {
        assertThatThrownBy { func() }.isInstanceOf(CannotPerformTransaction::class.java)
    }

    private fun assertValidTransaction(func: () -> Transaction) {
        assertThat(func()).isInstanceOf(Transaction::class.java)
    }

    private fun `verify trader interactions for single transaction`() {
        verify(trader, times(1)).performTransaction(any())
        verifyNoMoreInteractions(trader)
    }

    private fun `verify banker actioned only for account retrieval`() {
        verify(banker, times(1)).account(any())
        verifyNoMoreInteractions(banker)
    }

    private fun `verify banker actioned for savings update`() {
        verify(banker, times(1)).updateSavings(any(), any())
    }
}