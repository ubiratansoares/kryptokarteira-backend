package br.ufs.kryptokarteira.backend.domain

sealed class Currency(val label: String, val name: String) {
    object Brita : Currency("bta", "Brita")
    object Bitcoin : Currency("btc", "Bitcoin")
    object Real : Currency("blr", "Real")
}

object CurrencyFrom {
    operator fun invoke(label: String) = when (label) {
        "bta" -> Currency.Brita
        "btc" -> Currency.Bitcoin
        "blr" -> Currency.Real
        else -> throw UnknownInternalError()
    }
}