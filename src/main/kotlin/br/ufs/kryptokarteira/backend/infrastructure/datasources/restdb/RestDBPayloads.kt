package br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb

import br.ufs.kryptokarteira.backend.domain.TransactionLog
import com.google.gson.annotations.SerializedName

class CreateAccountPayload(
        val savings: List<SavingPayload>,
        val history: List<TransactionLog> = emptyList()
)

class UpdateSavingsPayload(
        val savings: List<SavingPayload>
)

class AccountPayload(
        @SerializedName("_id") val owner: String,
        val savings: List<SavingPayload>,
        val history: List<TransactionLog> = emptyList()
)

class SavingPayload(
        val name: String,
        val amount: Float
)
