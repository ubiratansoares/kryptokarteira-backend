package br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb

import br.ufs.kryptokarteira.backend.Configs
import br.ufs.kryptokarteira.backend.domain.DataForTransaction
import br.ufs.kryptokarteira.backend.domain.Investiment
import br.ufs.kryptokarteira.backend.domain.TransactionLog
import br.ufs.kryptokarteira.backend.infrastructure.networking.Header
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller
import br.ufs.kryptokarteira.backend.services.util.JsonSerializer

class RestDBDataSource(private val caller: RestCaller) {

    private val apikey by lazy { Configs.fromFileOrEnv("RESTDB_APIKEY") }

    fun createAccount(savings: List<Investiment>): AccountPayload {
        val apikeyHeader = Header("x-apikey", apikey)
        val mapped = CreateAccountPayload(
                savings = savings.map { SavingPayload(it.currency.label, it.amount) }
        )

        val jsonBody = JsonSerializer.asJson(mapped)
        return caller.post(DATABASE_URL, jsonBody, apikeyHeader, AccountPayload::class)
    }

    fun retrieveAccount(walletId: String): AccountPayload {
        val apikey = Header("x-apikey", apikey)
        val walletURL = "$DATABASE_URL/$walletId"
        return caller.get(walletURL, AccountPayload::class, apikey)
    }

    fun updateSavings(walletId: String, savings: List<Investiment>) {

        val apikeyHeader = Header("x-apikey", apikey)

        val mapped = UpdateSavingsPayload(
                savings = savings.map {
                    SavingPayload(it.currency.label, it.amount)
                }
        )

        val walletUrl = "$DATABASE_URL/$walletId"
        val jsonBody = JsonSerializer.asJson(mapped)
        caller.patch(walletUrl, jsonBody, apikeyHeader, AccountPayload::class)
    }

    fun registerTransaction(data: DataForTransaction, dateTime: String) {
        val apikeyHeader = Header("x-apikey", apikey)
        val walletId = data.owner
        val walletUrl = "$DATABASE_URL/$walletId"

        val log = TransactionLog(
                type = data.type,
                amount = data.amount,
                currency = data.currency.label,
                timestamp = dateTime
        )

        val jsonBody = "{\"\$push\": {\"history\": ${JsonSerializer.asJson(log)}}}"
        caller.put(walletUrl, jsonBody, apikeyHeader, AccountPayload::class)
    }

    private companion object {
        val DATABASE_URL = "https://kryptokarteira-3a93.restdb.io/rest/wallets"
    }
}