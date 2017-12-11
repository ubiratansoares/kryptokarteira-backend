package br.ufs.kryptokarteira.backend.tests.wallet

import br.ufs.kryptokarteira.backend.domain.KryptoBanker
import br.ufs.kryptokarteira.backend.services.WalletService
import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class WalletServiceTests {

    lateinit var service: WalletService
    val banker = KryptoBanker()

    @Before fun `before each test`() {
        service = WalletService(banker)
    }

    @Test fun `should create new wallet successfully`() {
        val operation = service.newWallet()

        assertThat(operation.statusCode).isEqualTo(200)

        assertThat(operation.result).contains(
                jsonArray(
                        jsonObject("name" to "Real", "amount" to 100000f),
                        jsonObject("name" to "Brita", "amount" to 0f),
                        jsonObject("name" to "Bitcoin", "amount" to 0f)
                ).toString()
        )
    }
}