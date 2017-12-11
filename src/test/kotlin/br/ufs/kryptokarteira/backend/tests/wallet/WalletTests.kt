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

        `transaction as buy may succeed`()

        assertValidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Bitcoin, amount = 2f)
        }

        `verify trader interactions buy single buy`()
    }


    @Test fun `can not buy criptocurrency with success when doesnt have money enough`() {

        assertInvalidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Bitcoin, amount = 20f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can only buy criptocurrency with positive amounts`() {

        assertInvalidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Bitcoin, amount = -10f)
        }

        assertInvalidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Brita, amount = 0.0f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can sell criptocurrency with success`() {

        `transaction as sell may succeed`()

        assertValidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Bitcoin, amount = 2f)
        }

        `verify trader interactions for single sell`()
    }


    @Test fun `can only sell criptocurrency with positive amounts`() {

        assertInvalidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Bitcoin, amount = -100f)
        }

        assertInvalidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Brita, amount = 0.0f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can only sell criptocurrency with success when amount is on wallet`() {

        assertInvalidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Brita, amount = 20.0f)
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

    private fun `transaction as buy may succeed`() {
        whenever(trader.buyMore(any(), any(), any())).thenReturn(Transaction("Success!"))
    }

    private fun `transaction as sell may succeed`() {
        whenever(trader.sell(any(), any(), any())).thenReturn(Transaction("Success!"))
    }

    private fun assertInvalidTransaction(func: () -> Transaction) {
        assertThatThrownBy {
            func()
        }.isInstanceOf(CannotPerformTransaction::class.java)
    }

    private fun assertValidTransaction(func: () -> Transaction) {
        assertThat(func()).isInstanceOf(Transaction::class.java)
    }

    private fun `verify trader interactions buy single buy`() {
        verify(trader, times(1)).buyMore(any(), any(), any())
        verify(trader, never()).sell(any(), any(), any())
        verifyNoMoreInteractions(trader)
    }

    private fun `verify trader interactions for single sell`() {
        verify(trader, times(1)).sell(any(), any(), any())
        verify(trader, never()).buyMore(any(), any(), any())
        verifyNoMoreInteractions(trader)
    }

}