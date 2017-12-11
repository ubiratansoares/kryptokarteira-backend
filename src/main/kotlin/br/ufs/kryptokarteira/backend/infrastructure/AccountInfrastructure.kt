package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.*

class AccountInfrastructure : AccountManager {

    private val accounts = mutableListOf<BankAccount>()

    init {
        accounts += BankAccount(
                owner = "6a1438ee-abf2-4f26-bc95-fabb0361a080",
                savings = listOf(
                        Investiment(Currency.Real, 45678.8f),
                        Investiment(Currency.Brita, 32456.1f),
                        Investiment(Currency.Bitcoin, 34.3f)
                )
        )
    }

    override fun createAccount(account: BankAccount): BankAccount {

        if (accounts.isEmpty()) throw ExternalServiceContractError()

        if (accountRegistred(account.owner)) throw ExternalServiceContractError()

        accounts += account
        return account
    }

    override fun accountForOwner(owner: String): BankAccount {

        if (accounts.isEmpty()) throw ExternalServiceContractError()

        if (accountNotRegistred(owner)) throw ExternalServiceContractError()

        return accounts.first { it.owner == owner }
    }

    override fun updateSavings(owner: String, savings: List<Investiment>) {
        if (accounts.isEmpty()) throw ExternalServiceContractError()

        if (accountNotRegistred(owner)) throw ExternalServiceContractError()

        accounts.first { it.owner == owner }.copy(savings = savings)
    }

    private fun accountNotRegistred(owner: String): Boolean {
        return accounts.none { it.owner == owner }
    }

    private fun accountRegistred(owner: String): Boolean {
        return !accountNotRegistred(owner)
    }
}