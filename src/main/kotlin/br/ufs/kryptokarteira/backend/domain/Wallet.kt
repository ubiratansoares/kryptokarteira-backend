package br.ufs.kryptokarteira.backend.domain

class Wallet(
        private val bankAccount: BankAccount,
        private val trader: CryptoCurrencyTrader,
        private val broker: PricesBroker) {

    fun buyCriptoCurrency(wantToBuy: Currency, amount: Float): Transaction {
        val investments = bankAccount.savings
        val remainingCash = investments.first { it.currency == Currency.Real }
        val ableToBuy = checkIfBuyAllowed(remainingCash, wantToBuy, amount)

        return when (ableToBuy) {
            true -> trader.buyMore(bankAccount.owner, wantToBuy, amount)
            false -> throw CannotPerformTransaction()
        }
    }

    fun sellCriptoCurrency(wantToSell: Currency, amount: Float): Transaction {
        val ableToSell = checkIfSellingAllowed(wantToSell, amount)

        return when (ableToSell) {
            true -> trader.sell(bankAccount.owner, wantToSell, amount)
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