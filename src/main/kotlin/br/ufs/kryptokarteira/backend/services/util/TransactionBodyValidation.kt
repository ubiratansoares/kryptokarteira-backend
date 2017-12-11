package br.ufs.kryptokarteira.backend.services.util

import br.ufs.kryptokarteira.backend.domain.Currency
import br.ufs.kryptokarteira.backend.domain.Currency.Bitcoin
import br.ufs.kryptokarteira.backend.domain.Currency.Brita
import br.ufs.kryptokarteira.backend.domain.DataForTransaction
import br.ufs.kryptokarteira.backend.domain.UnknownInternalError
import br.ufs.kryptokarteira.backend.services.input.NewTransactionBody

object TransactionBodyValidation {

    operator fun invoke(owner: String?, rawTransactionBody: String?): DataForTransaction {
        val walletOwner = owner?.let { it } ?: throw UnknownInternalError()
        val validBody = rawTransactionBody.let { it } ?: throw MissingTransactionBody()
        val body = JsonSerializer.fromJson(validBody, NewTransactionBody::class)
        val type = body.type?.let { validateType(it) } ?: throw MissingTransactionType()
        val currency = body.currency?.let { validateCurrency(it) } ?: throw MissingCurrency()
        val amount = body.amount?.let { validateAmount(it) } ?: throw MissingAmount()

        return DataForTransaction(walletOwner, type, currency, amount)
    }

    private fun validateAmount(amount: Float): Float {
        return if (amount > 0.0f) amount else throw InvalidAmount()
    }

    private fun validateCurrency(currency: String): Currency {
        val validCurrency = with(currency) {
            if (contentEquals(Brita.label) || contentEquals(Bitcoin.label)) this else "Invalid"
        }

        return when (validCurrency) {
            Brita.label -> Brita
            Bitcoin.label -> Bitcoin
            else -> throw InvalidCurrency()
        }
    }

    private fun validateType(type: String): String {
        val validType = with(type) {
            contentEquals(DataForTransaction.BUY_OPERATION) ||
                    contentEquals(DataForTransaction.SELL_OPERATION)
        }

        return if (validType) type else throw InvalidTransactionType()
    }
}