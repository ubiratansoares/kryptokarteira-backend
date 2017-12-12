package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.AccountManager
import br.ufs.kryptokarteira.backend.domain.BankAccount
import br.ufs.kryptokarteira.backend.domain.CurrencyFrom
import br.ufs.kryptokarteira.backend.domain.Investiment
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.AccountPayload
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.RestDBDataSource

class AccountInfrastructure(
        private val dataSource: RestDBDataSource) : AccountManager {

    override fun createAccount(savings: List<Investiment>): BankAccount {
        val payload = dataSource.createAccount(savings)
        return accountFromPayload(payload)
    }

    override fun accountForOwner(owner: String): BankAccount {
        val payload = dataSource.retrieveAccount(owner)
        return accountFromPayload(payload)
    }

    override fun updateSavings(owner: String, savings: List<Investiment>) {
        dataSource.updateSavings(owner, savings)
    }

    private fun accountFromPayload(payload: AccountPayload): BankAccount {
        return BankAccount(
                owner = payload.owner,
                savings = payload.savings.map {
                    Investiment(CurrencyFrom(it.name), it.amount)
                }
        )
    }

}