package br.ufs.kryptokarteira.backend.domain

import br.ufs.kryptokarteira.backend.domain.Currency.*
import java.util.*

class KryptoBanker {

    fun newAccount(): BankAccount {
        val owner = UUID.randomUUID().toString()
        val savings = listOf(
                Investiment(Real, INITIAL_GIVING),
                Investiment(Brita, NOTHING),
                Investiment(Bitcoin, NOTHING)
        )
        return BankAccount(owner, savings)
    }

    private companion object {
        val INITIAL_GIVING = 100000f
        val NOTHING = 0.0f
    }
}