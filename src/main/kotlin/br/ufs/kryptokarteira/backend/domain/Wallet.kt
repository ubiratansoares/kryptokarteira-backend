package br.ufs.kryptokarteira.backend.domain

class Wallet(
        private val bankAccount: BankAccount,
        private val trader: CryptoCurrencyTrader,
        private val broker: PricesBroker) {

    fun buyCryptoCurrency(wantToBuy: Currency, amount: Float): Transaction {
        val investments = bankAccount.savings
        val remainingCash = investments.first { it.currency == Currency.Real }
        val ableToBuy = checkIfBuyAllowed(remainingCash, wantToBuy, amount)

        val transactionData = DataForTransaction(
                bankAccount.owner,
                type = DataForTransaction.BUY_OPERATION,
                currency = wantToBuy,
                amount = amount
        )
        return when (ableToBuy) {
            true -> trader.performTransaction(transactionData)
            false -> throw CannotPerformTransaction()
        }
    }

    fun sellCryptoCurrency(wantToSell: Currency, amount: Float): Transaction {
        val ableToSell = checkIfSellingAllowed(wantToSell, amount)

        val transactionData = DataForTransaction(
                bankAccount.owner,
                type = DataForTransaction.SELL_OPERATION,
                currency = wantToSell,
                amount = amount
        )

        return when (ableToSell) {
            true -> trader.performTransaction(transactionData)
            false -> throw CannotPerformTransaction()
        }
    }

    private fun checkIfSellingAllowed(wantToSell: Currency, desiredAmount: Float): Boolean {
        if (desiredAmount <= 0.0f) return false
        val investmentForCurrency = bankAccount.savings.first { it.currency == wantToSell }
        return desiredAmount <= investmentForCurrency.amount
    }

    private fun checkIfBuyAllowed(
            remainingCash: Investiment,
            desired: Currency,
            desiredAmount: Float): Boolean {

        if (desiredAmount <= 0.0f) return false

        val updatedPriceForCurrency = broker.lastestPrices().first { it.currency == desired }
        val maxAllowed = remainingCash.amount / updatedPriceForCurrency.buyPrice

        return desiredAmount <= maxAllowed

    }
}