package br.ufs.kryptokarteira.backend.domain

interface AccountManager {

    fun createAccount(account: BankAccount): BankAccount

    fun accountForOwner(owner: String): BankAccount

    fun updateSavings(owner: String, savings: List<Investiment>)
}