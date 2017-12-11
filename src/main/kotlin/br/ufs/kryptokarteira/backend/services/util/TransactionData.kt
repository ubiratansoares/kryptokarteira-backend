package br.ufs.kryptokarteira.backend.services.util

import br.ufs.kryptokarteira.backend.domain.Currency

data class TransactionData(
        val type: String,
        val currency: Currency,
        val amount: Float
) {
    companion object {
        val TRANSACTION_BUY = "buy"
        val TRANSACTION_SELL = "sell"
    }
}