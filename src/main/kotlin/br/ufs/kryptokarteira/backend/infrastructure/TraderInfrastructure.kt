package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.CryptoCurrencyTrader
import br.ufs.kryptokarteira.backend.domain.DataForTransaction
import br.ufs.kryptokarteira.backend.domain.Transaction


class TraderInfrastructure : CryptoCurrencyTrader {

    override fun performTransaction(data: DataForTransaction): Transaction {
        return Transaction("Success!")
    }

}