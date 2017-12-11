package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.services.output.SavingPayload
import br.ufs.kryptokarteira.backend.services.output.TransactionResultPayload
import br.ufs.kryptokarteira.backend.services.output.WalletPayload
import br.ufs.kryptokarteira.backend.services.util.InvalidTransactionType
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation
import br.ufs.kryptokarteira.backend.services.util.TransactionBodyValidation
import br.ufs.kryptokarteira.backend.services.util.TransactionData.Companion.TRANSACTION_BUY
import br.ufs.kryptokarteira.backend.services.util.TransactionData.Companion.TRANSACTION_SELL

class WalletService(
        private val banker: KryptoBanker,
        private val trader: CriptoCurrencyTrader) {

    private val mapper = JsonSerializer

    fun newWallet(): ServiceOperation {
        val account = banker.newAccount()
        val output = walletPayload(account)
        return ServiceOperation(200, mapper.asJson(output))
    }

    fun walletByOwner(owner: String): ServiceOperation {
        val account = banker.wallet(owner)
        val output = walletPayload(account)
        return ServiceOperation(200, mapper.asJson(output))
    }

    fun newTransaction(id: String?, rawBody: String?): ServiceOperation {
        val wallet = id?.let { it } ?: throw UnknownInternalError()
        val (type, currency, amount) = TransactionBodyValidation(rawBody)

        val transaction = when (type) {
            TRANSACTION_BUY -> trader.buyMore(wallet, currency, amount)
            TRANSACTION_SELL -> trader.sell(wallet, currency, amount)
            else -> throw InvalidTransactionType()
        }

        if (transaction is Transaction.Successfull) {
            val output = TransactionResultPayload("Success!")
            return ServiceOperation(201, mapper.asJson(output))
        }

        throw CannotPerformTransaction()
    }

    private fun walletPayload(account: BankAccount): WalletPayload {
        return with(account) {
            WalletPayload(
                    owner = account.owner,
                    savings = savings.map { SavingPayload(it.currency.name, it.amount) }
            )
        }
    }

}