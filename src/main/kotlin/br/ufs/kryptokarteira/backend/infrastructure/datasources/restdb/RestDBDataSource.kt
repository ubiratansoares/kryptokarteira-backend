package br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb

import br.ufs.kryptokarteira.backend.domain.BankAccount
import br.ufs.kryptokarteira.backend.infrastructure.networking.Header
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer

class RestDBDataSource(private val caller: RestCaller) {

    fun createAccount(account: BankAccount) {
        val apikeyHeader = Header("x-apikey", API_KEY)
        val mapped = AccountPayload(
                owner = account.owner,
                savings = account.savings.map { SavingPayload(it.currency.label, it.amount) }
        )
        val jsonBody = JsonSerializer.asJson(mapped)
        caller.post(DATABASE_URL, jsonBody, apikeyHeader, AccountPayload::class)
    }

    private companion object {
        val DATABASE_URL = "https://kryptokarteira-3a93.restdb.io/rest/wallets"
        val API_KEY = "5f2f7a83fb0b068e7b4b0e6921285dc489f66"
    }
}