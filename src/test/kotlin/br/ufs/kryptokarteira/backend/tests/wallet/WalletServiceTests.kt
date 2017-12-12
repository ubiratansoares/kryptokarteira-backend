package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.domain.Currency.*
import br.ufs.kryptokarteira.backend.services.WalletService
import com.github.salomonbrys.kotson.jsonObject
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class WalletServiceTests {

    lateinit var service: WalletService
    lateinit var banker: CryptoBanker
    lateinit var trader: CryptoCurrencyTrader
    lateinit var broker: PricesBroker

    val fakeAccount = BankAccount(
            owner = "Bira",
            history = emptyList(),
            savings = listOf(
                    Investiment(Real, 50000f),
                    Investiment(Brita, 10.0f),
                    Investiment(Bitcoin, 4.4f)
            )
    )

    val fakePricing = listOf(
            Pricing(Bitcoin, 10f, 10f),
            Pricing(Brita, 1f, 1f)
    )

    @Before fun `before each test`() {
        banker = mock()
        trader = mock()
        broker = mock()
        service = WalletService(banker, trader, broker)
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

    @Test fun `should perform buy transaction with success`() {
        `setup transaction collaborators`()

        val rawOwner = "Bira"
        val rawBody = buyBitcoins()

        val operation = service.newTransaction(rawOwner, rawBody)
        assertThat(operation.statusCode).isEqualTo(201)
    }

    @Test fun `should perform sell transaction with success`() {
        `setup transaction collaborators`()

        val rawOwner = "Bira"
        val rawBody = sellBritas()

        val operation = service.newTransaction(rawOwner, rawBody)
        assertThat(operation.statusCode).isEqualTo(201)
    }


    private fun `setup transaction collaborators`() {
        whenever(banker.account(any())).thenReturn(fakeAccount)
        whenever(trader.performTransaction(any())).thenReturn(Transaction("Done"))
        whenever(broker.lastestPrices()).thenReturn(fakePricing)
    }

    private fun buyBitcoins() =
            jsonObject(
                    "type" to "buy",
                    "currency" to "btc",
                    "amount" to 2.1f
            ).toString()

    private fun sellBritas() =
            jsonObject(
                    "type" to "sell",
                    "currency" to "bta",
                    "amount" to 5
            ).toString()

}