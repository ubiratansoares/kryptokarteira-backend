package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.CryptoCurrencyTrader
import br.ufs.kryptokarteira.backend.domain.KryptoBanker
import br.ufs.kryptokarteira.backend.services.WalletService
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test


class WalletServiceTests {

    lateinit var service: WalletService
    lateinit var banker: KryptoBanker
    lateinit var trader: CryptoCurrencyTrader

    @Before fun `before each test`() {
        banker = mock()
        trader = mock()
        service = WalletService(banker, trader)
    }

    @Test fun `should create new wallet successfully`() {

        // TODO : fix me
//        val operation = service.newWallet()
//        assertThat(operation.statusCode).isEqualTo(200)
    }
}