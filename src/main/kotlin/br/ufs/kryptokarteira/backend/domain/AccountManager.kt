package br.ufs.kryptokarteira.backend.domain

interface AccountManager {

    fun createAccount(savings: List<Investiment>): BankAccount

    fun accountForOwner(owner: String): BankAccount

    fun updateSavings(owner: String, savings: List<Investiment>)
}