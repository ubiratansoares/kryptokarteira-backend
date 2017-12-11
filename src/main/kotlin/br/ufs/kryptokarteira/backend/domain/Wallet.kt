package br.ufs.kryptokarteira.backend.domain

class Wallet(
        private val owner: String,
        private val manager: PortfolioManager,
        private val trader: CriptoCurrencyTrader,
        private val broker: PricesBroker) {

    fun buyCriptoCurrency(wantToBuy: Currency, amount: Float): Transaction {
        val investments = manager.savings()
        val remainingCash = investments.first { it.currency == Currency.Real }
        val ableToBuy = checkIfBuyAllowed(remainingCash, wantToBuy, amount)

        return if (ableToBuy) trader.buyMore(owner, wantToBuy, amount) else Transaction.Invalid()
    }

    fun sellCriptoCurrency(wantToSell: Currency, amount: Float): Transaction {
        val ableToSell = checkIfSellingAllowed(wantToSell, amount)
        return if (ableToSell) trader.sell(owner, wantToSell, amount) else Transaction.Invalid()
    }

    private fun checkIfSellingAllowed(wantToSell: Currency, desiredAmount: Float): Boolean {
        if (desiredAmount <= 0.0f) return false
        val investmentForCurrency = manager.savings().first { it.currency == wantToSell }
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