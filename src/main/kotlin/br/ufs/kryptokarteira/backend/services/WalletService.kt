package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.CryptoBanker
import br.ufs.kryptokarteira.backend.domain.CryptoCurrencyTrader
import br.ufs.kryptokarteira.backend.domain.DataForTransaction.Companion.BUY_OPERATION
import br.ufs.kryptokarteira.backend.domain.DataForTransaction.Companion.SELL_OPERATION
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.domain.Wallet
import br.ufs.kryptokarteira.backend.services.output.TransactionResultPayload
import br.ufs.kryptokarteira.backend.services.output.WalletPayloadFromBankAccount
import br.ufs.kryptokarteira.backend.services.util.InvalidTransactionType
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation
import br.ufs.kryptokarteira.backend.services.util.TransactionBodyValidation

class WalletService(
        private val banker: CryptoBanker,
        private val trader: CryptoCurrencyTrader,
        private val broker: PricesBroker) {

    private val mapper = JsonSerializer

    fun newWallet(): ServiceOperation {
        val account = banker.newAccount()
        val output = WalletPayloadFromBankAccount(account)
        return ServiceOperation(200, mapper.asJson(output))
    }

    fun walletByOwner(owner: String): ServiceOperation {
        val account = banker.account(owner)
        val output = WalletPayloadFromBankAccount(account)
        return ServiceOperation(200, mapper.asJson(output))
    }

    fun newTransaction(rawOwner: String?, rawBody: String?): ServiceOperation {
        val (owner, type, currency, amount) = TransactionBodyValidation(rawOwner, rawBody)
        val wallet = Wallet(owner, banker, trader, broker)

        val transaction = when (type) {
            BUY_OPERATION -> wallet.buyCryptoCurrency(currency, amount)
            SELL_OPERATION -> wallet.sellCryptoCurrency(currency, amount)
            else -> throw InvalidTransactionType()
        }

        val output = TransactionResultPayload(transaction.message)
        return ServiceOperation(201, mapper.asJson(output))
    }


}