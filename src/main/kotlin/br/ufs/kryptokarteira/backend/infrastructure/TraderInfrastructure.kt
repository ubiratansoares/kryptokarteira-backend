package br.ufs.kryptokarteira.backend.infrastructure

import br.ufs.kryptokarteira.backend.domain.CryptoCurrencyTrader
import br.ufs.kryptokarteira.backend.domain.DataForTransaction
import br.ufs.kryptokarteira.backend.domain.Transaction
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.RestDBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.util.AsDomainError
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME


class TraderInfrastructure(private val datasource: RestDBDataSource) : CryptoCurrencyTrader {

    override fun performTransaction(data: DataForTransaction): Transaction {
        val dateTime = LocalDateTime.now().format(ISO_DATE_TIME)

        try {
            datasource.registerTransaction(data, dateTime)
            return Transaction("Success!")
        } catch (incoming: Throwable) {
            throw AsDomainError(incoming)
        }
    }

}