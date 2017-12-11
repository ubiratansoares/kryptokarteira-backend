package br.ufs.kryptokarteira.backend.domain

import br.ufs.kryptokarteira.backend.domain.Currency.*
import java.util.*

class KryptoBanker(private val accountManager: AccountManager) {

    fun newAccount(): BankAccount {
        val owner = UUID.randomUUID().toString()
        val savings = listOf(
                Investiment(Real, INITIAL_GIVING),
                Investiment(Brita, NOTHING),
                Investiment(Bitcoin, NOTHING)
        )

        val newAccount = BankAccount(owner, savings)
        return accountManager.createAccount(newAccount)

    }

    fun account(owner: String): BankAccount {
        return accountManager.accountForOwner(owner)
    }

    private companion object {
        val INITIAL_GIVING = 100000f
        val NOTHING = 0.0f
    }
}