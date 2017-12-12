package br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb

import com.google.gson.annotations.SerializedName

class CreateAccountPayload(
        val savings: List<SavingPayload>,
        val history: List<TransactionPayload> = emptyList()
)

class UpdateSavingsPayload(
        val savings: List<SavingPayload>
)

class AccountPayload(
        @SerializedName("_id") val owner: String,
        val savings: List<SavingPayload>,
        val history: List<TransactionPayload> = emptyList()
)

class SavingPayload(
        val name: String,
        val amount: Float
)

class TransactionPayload(
        val type: String,
        val currency: String,
        val amount: Float,
        val dateTime: String
)