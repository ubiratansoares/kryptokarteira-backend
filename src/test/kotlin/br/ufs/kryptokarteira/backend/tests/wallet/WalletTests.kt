package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.domain.Currency.*
import com.nhaarman.mockito_kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

class WalletTests {

    lateinit var manager: PortfolioManager
    lateinit var trader: CriptoCurrencyTrader
    lateinit var broker: PricesBroker
    lateinit var wallet: Wallet

    @Before fun `before each test`() {
        manager = mock()
        trader = mock()
        broker = mock()

        val owner = UUID.randomUUID().toString()
        wallet = Wallet(owner, manager, trader, broker)
    }

    @Test fun `can buy criptocurrency with success when money available`() {

        `setup savings and pricing`()
        `transaction as buy may succeed`()

        assertValidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Bitcoin, amount = 2f)
        }

        `verify trader interactions buy single buy`()
    }


    @Test fun `can not buy criptocurrency with success when doesnt have money enough`() {

        `setup savings and pricing`()

        assertInvalidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Bitcoin, amount = 20f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can only buy criptocurrency with positive amounts`() {

        `setup savings and pricing`()

        assertInvalidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Bitcoin, amount = -10f)
        }

        assertInvalidTransaction {
            wallet.buyCriptoCurrency(wantToBuy = Brita, amount = 0.0f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can sell criptocurrency with success`() {

        `setup savings and pricing`()
        `transaction as sell may succeed`()

        assertValidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Bitcoin, amount = 2f)
        }

        `verify trader interactions for single sell`()
    }


    @Test fun `can only sell criptocurrency with positive amounts`() {

        `setup savings and pricing`()

        assertInvalidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Bitcoin, amount = -100f)
        }


        assertInvalidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Brita, amount = 0.0f)
        }

        verifyZeroInteractions(trader)
    }

    @Test fun `can only sell criptocurrency with success when amount is on wallet`() {

        `setup savings and pricing`()

        assertInvalidTransaction {
            wallet.sellCriptoCurrency(wantToSell = Brita, amount = 20.0f)
        }

        verifyZeroInteractions(trader)
    }

    private fun `setup savings and pricing`() {
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

        whenever(manager.savings()).thenReturn(savings)
    }

    private fun `transaction as buy may succeed`() {
        whenever(trader.buyMore(any(), any(), any())).thenReturn(Transaction.Successfull())
    }

    private fun `transaction as sell may succeed`() {
        whenever(trader.sell(any(), any(), any())).thenReturn(Transaction.Successfull())
    }

    private fun assertInvalidTransaction(func: () -> Transaction) {
        assertThat(func()).isInstanceOf(Transaction.Invalid::class.java)
    }

    private fun assertValidTransaction(func: () -> Transaction) {
        assertThat(func()).isInstanceOf(Transaction.Successfull::class.java)
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