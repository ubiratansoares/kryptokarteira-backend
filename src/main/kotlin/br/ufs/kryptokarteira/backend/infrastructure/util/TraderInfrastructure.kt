package br.ufs.kryptokarteira.backend.infrastructure.util

import br.ufs.kryptokarteira.backend.domain.CriptoCurrencyTrader
import br.ufs.kryptokarteira.backend.domain.Currency
import br.ufs.kryptokarteira.backend.domain.Transaction

class TraderInfrastructure : CriptoCurrencyTrader {

    override fun buyMore(owner: String, currency: Currency, amount: Float): Transaction {
        TODO("not implemented")
    }

    override fun sell(owner: String, currency: Currency, amount: Float): Transaction {
        TODO("not implemented")
    }

}