package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.*
import br.ufs.kryptokarteira.backend.domain.Currency.Bitcoin
import br.ufs.kryptokarteira.backend.domain.Currency.Brita
import br.ufs.kryptokarteira.backend.services.input.NewTransactionBody
import br.ufs.kryptokarteira.backend.services.output.SavingPayload
import br.ufs.kryptokarteira.backend.services.output.TransactionResultPayload
import br.ufs.kryptokarteira.backend.services.output.WalletPayload
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation

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

    private fun walletPayload(account: BankAccount): WalletPayload {
        return with(account) {
            WalletPayload(
                    owner = account.owner,
                    savings = savings.map { SavingPayload(it.currency.name, it.amount) }
            )
        }
    }

    fun newTransaction(id: String?, rawBody: String?): ServiceOperation {
        val wallet = id?.let { it } ?: throw UnknownInternalError()
        val validBody = rawBody.let { it } ?: throw MissingTransactionBody()

        val body = mapper.fromJson(validBody, NewTransactionBody::class)

        val type = body.type?.let { validateType(it) } ?: throw MissingTransactionType()
        val currency = body.currency?.let { validateCurrency(it) } ?: throw MissingCurrency()
        val amount = body.amount?.let { validateAmount(it) } ?: throw MissingAmount()


        val transaction = when (type) {
            "buy" -> trader.buyMore(wallet, currency, amount)
            "sell" -> trader.sell(wallet, currency, amount)
            else -> throw InvalidTransactionType()
        }

        if (transaction is Transaction.Successfull) {
            val output = TransactionResultPayload("Success!")
            return ServiceOperation(200, mapper.asJson(output))
        }

        throw CannotPerformTransaction()
    }

    private fun validateAmount(amount: Float): Float {
        return if (amount > 0.0f) amount else throw InvalidAmount()
    }

    private fun validateCurrency(currency: String): Currency {
        val validCurrency = with(currency) {
            contentEquals(Brita.label) || contentEquals(Bitcoin.label)
        }

        if (validCurrency) {
            return when (currency) {
                Brita.label -> Brita
                Bitcoin.label -> Bitcoin
                else -> throw InvalidCurrency()
            }
        } else throw InvalidCurrency()
    }

    private fun validateType(type: String): String {
        val validType = with(type) {
            contentEquals("buy") || contentEquals("sell")
        }

        return if (validType) type else throw InvalidTransactionType()
    }

}