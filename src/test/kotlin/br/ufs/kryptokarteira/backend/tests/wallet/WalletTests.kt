package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.domain.Currency.*
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

class WalletTests {

    lateinit var bankAccount: BankAccount
    lateinit var trader: CryptoCurrencyTrader
    lateinit var broker: PricesBroker
    lateinit var wallet: Wallet

    @Before fun `before each test`() {
        trader = mock()
        broker = mock()
        bankAccount = setupBankAccount()
        wallet = Wallet(bankAccount, trader, broker)
    }

    @Test fun `can buy criptocurrency with success when money available`() {

        `transaction will succeed`()

        assertValidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Bitcoin, amount = 2f)
        }

        `verify trader interactions for single transaction`()
    }


    @Test fun `can not buy criptocurrency with success when doesnt have money enough`() {

        assertInvalidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Bitcoin, amount = 20f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can only buy criptocurrency with positive amounts`() {

        assertInvalidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Bitcoin, amount = -10f)
        }

        assertInvalidTransaction {
            wallet.buyCryptoCurrency(wantToBuy = Brita, amount = 0.0f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can sell criptocurrency with success`() {

        `transaction will succeed`()

        assertValidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Bitcoin, amount = 2f)
        }

        `verify trader interactions for single transaction`()
    }


    @Test fun `can only sell criptocurrency with positive amounts`() {

        assertInvalidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Bitcoin, amount = -100f)
        }

        assertInvalidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Brita, amount = 0.0f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can only sell criptocurrency with success when amount is on wallet`() {

        assertInvalidTransaction {
            wallet.sellCryptoCurrency(wantToSell = Brita, amount = 20.0f)
        }

        verifyZeroInteractions(trader)
    }

    private fun setupBankAccount(): BankAccount {
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

        return BankAccount("Bira", savings)
    }

    private fun `transaction will succeed`() {
        whenever(trader.performTransaction(any())).thenReturn(Transaction("Success!"))
    }

    private fun assertInvalidTransaction(func: () -> Transaction) {
        assertThatThrownBy {
            func()
        }.isInstanceOf(CannotPerformTransaction::class.java)
    }

    private fun assertValidTransaction(func: () -> Transaction) {
        assertThat(func()).isInstanceOf(Transaction::class.java)
    }

    private fun `verify trader interactions for single transaction`() {
        verify(trader, times(1)).performTransaction(any())
        verifyNoMoreInteractions(trader)
    }

}