package br.ufs.kryptokarteira.backend.domain

interface CryptoCurrencyTrader {

    fun performTransaction(data: DataForTransaction): Transaction

}