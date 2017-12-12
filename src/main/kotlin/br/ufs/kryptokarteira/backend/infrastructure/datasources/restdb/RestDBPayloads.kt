package br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb

import com.google.gson.annotations.SerializedName

class CreateAccountPayload(
        val savings: List<SavingPayload>
)

class AccountPayload(
        @SerializedName("_id") val owner: String,
        val savings: List<SavingPayload>
)

class SavingPayload(
        val name: String,
        val amount: Float
)