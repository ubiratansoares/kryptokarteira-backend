package br.ufs.kryptokarteira.backend.domain

sealed class Currency(val label: String, val name: String) {
    object Brita : Currency("bta", "Brita")
    object Bitcoin : Currency("btc", "Bitcoin")
    object Real : Currency("blr", "Real")
}