package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.AccountManager
import br.ufs.kryptokarteira.backend.domain.KryptoBanker
import br.ufs.kryptokarteira.backend.services.WalletService
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test


class WalletServiceTests {

    lateinit var service: WalletService
    lateinit var accountManager: AccountManager

    @Before fun `before each test`() {
        accountManager = mock()
        val banker = KryptoBanker(accountManager)
        service = WalletService(banker)
    }

    @Test fun `should create new wallet successfully`() {

        // TODO : fix me
//        val operation = service.newWallet()
//        assertThat(operation.statusCode).isEqualTo(200)
    }
}