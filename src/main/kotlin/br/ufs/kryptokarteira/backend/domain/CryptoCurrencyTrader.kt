package br.ufs.kryptokarteira.backend.domain

interface CryptoCurrencyTrader {

    fun buyMore(owner: String, currency: Currency, amount: Float): Transaction

    fun sell(owner: String, currency: Currency, amount: Float): Transaction
}