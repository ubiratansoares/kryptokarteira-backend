package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.AccountManager
import br.ufs.kryptokarteira.backend.domain.BankAccount
import br.ufs.kryptokarteira.backend.domain.CurrencyFrom
import br.ufs.kryptokarteira.backend.domain.Investiment
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.AccountPayload
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.RestDBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.util.AsDomainError

class AccountInfrastructure(
        private val dataSource: RestDBDataSource) : AccountManager {

    override fun createAccount(savings: List<Investiment>): BankAccount {

        try {
            val payload = dataSource.createAccount(savings)
            return accountFromPayload(payload)
        } catch (incoming: Throwable) {
            throw AsDomainError(incoming)
        }
    }

    override fun accountForOwner(owner: String): BankAccount {

        try {
            val payload = dataSource.retrieveAccount(owner)
            return accountFromPayload(payload)
        } catch (incoming: Throwable) {
            throw AsDomainError(incoming)
        }
    }

    override fun updateSavings(owner: String, savings: List<Investiment>) {

        try {
            dataSource.updateSavings(owner, savings)
        } catch (incoming: Throwable) {
            throw AsDomainError(incoming)
        }

    }

    private fun accountFromPayload(payload: AccountPayload): BankAccount {
        return BankAccount(
                owner = payload.owner,
                history = payload.history,
                savings = payload.savings.map {
                    Investiment(CurrencyFrom(it.name), it.amount)
                }
        )
    }

}