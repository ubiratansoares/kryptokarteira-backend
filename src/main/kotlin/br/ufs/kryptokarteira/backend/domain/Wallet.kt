package br.ufs.kryptokarteira.backend.domain

import br.ufs.kryptokarteira.backend.domain.Currency.Real

class Wallet(
        private val owner: String,
        private val banker: CryptoBanker,
        private val trader: CryptoCurrencyTrader,
        private val broker: PricesBroker) {

    private val lastestPrices by lazy { broker.lastestPrices() }

    fun buyCryptoCurrency(wantToBuy: Currency, amount: Float): Transaction {
        val account = banker.account(owner)
        val investments = account.savings
        val remainingCash = investments.first { it.currency == Real }
        val ableToBuy = checkIfBuyAllowed(remainingCash, wantToBuy, amount)

        val transactionData = DataForTransaction(
                owner,
                type = DataForTransaction.BUY_OPERATION,
                currency = wantToBuy,
                amount = amount
        )

        return when (ableToBuy) {
            false -> throw CannotPerformTransaction()
            true -> {
                val result = trader.performTransaction(transactionData)
                val newSavings = savingsAfterBuy(investments, wantToBuy, amount)
                banker.updateSavings(owner, newSavings)
                result
            }
        }
    }

    fun sellCryptoCurrency(wantToSell: Currency, amount: Float): Transaction {

        val account = banker.account(owner)
        val investments = account.savings
        val ableToSell = checkIfSellingAllowed(investments, wantToSell, amount)

        val transactionData = DataForTransaction(
                owner,
                type = DataForTransaction.SELL_OPERATION,
                currency = wantToSell,
                amount = amount
        )

        return when (ableToSell) {
            false -> throw CannotPerformTransaction()
            true -> {
                val result = trader.performTransaction(transactionData)
                val newSavings = savingsAfterSell(investments, wantToSell, amount)
                banker.updateSavings(owner, newSavings)
                result
            }

        }
    }

    private fun savingsAfterBuy(
            investments: List<Investiment>,
            buyed: Currency,
            quantity: Float): List<Investiment> {

        val updatedPriceForCurrency = lastestPrices.first { it.currency == buyed }
        val debit = updatedPriceForCurrency.buyPrice * quantity

        return investments.map {
            when (it.currency) {
                buyed -> Investiment(buyed, it.amount + quantity)
                Real -> Investiment(Real, it.amount - debit)
                else -> Investiment(it.currency, it.amount)
            }
        }
    }

    private fun savingsAfterSell(
            investments: List<Investiment>,
            target: Currency,
            quantity: Float): List<Investiment> {

        val updatedPriceForCurrency = lastestPrices.first { it.currency == target }
        val profit = updatedPriceForCurrency.sellPrice * quantity

        return investments.map {
            when (it.currency) {
                target -> Investiment(target, it.amount - quantity)
                Real -> Investiment(Real, it.amount + profit)
                else -> Investiment(it.currency, it.amount)
            }
        }
    }

    private fun checkIfSellingAllowed(investments: List<Investiment>,
                                      wantToSell: Currency,
                                      desiredAmount: Float): Boolean {

        if (desiredAmount <= 0.0f) return false
        val investmentForCurrency = investments.first { it.currency == wantToSell }
        return desiredAmount <= investmentForCurrency.amount
    }

    private fun checkIfBuyAllowed(
            remainingCash: Investiment,
            desired: Currency,
            desiredAmount: Float): Boolean {

        if (desiredAmount <= 0.0f) return false

        val updatedPriceForCurrency = lastestPrices.first { it.currency == desired }
        val maxAllowed = remainingCash.amount / updatedPriceForCurrency.buyPrice

        return desiredAmount <= maxAllowed

    }
}