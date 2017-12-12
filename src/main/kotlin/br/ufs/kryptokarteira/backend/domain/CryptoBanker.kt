package br.ufs.kryptokarteira.backend.domain

import br.ufs.kryptokarteira.backend.domain.Currency.*

class CryptoBanker(private val accountManager: AccountManager) {

    fun newAccount(): BankAccount {
        val savings = listOf(
                Investiment(Real, INITIAL_GIVING),
                Investiment(Brita, NOTHING),
                Investiment(Bitcoin, NOTHING)
        )

        return accountManager.createAccount(savings)
    }

    fun account(owner: String): BankAccount {
        return accountManager.accountForOwner(owner)
    }

    private companion object {
        val INITIAL_GIVING = 100000f
        val NOTHING = 0.0f
    }
}