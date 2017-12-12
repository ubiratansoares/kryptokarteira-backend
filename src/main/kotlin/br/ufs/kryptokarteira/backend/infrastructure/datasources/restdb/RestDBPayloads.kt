package br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb

class AccountPayload(
        val owner: String,
        val savings: List<SavingPayload>
)

class SavingPayload(
        val name: String,
        val amount: Float
)