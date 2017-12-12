package br.ufs.kryptokarteira.backend.domain

data class BankAccount(
        val owner: String,
        val savings: List<Investiment>,
        val history : List<TransactionLog>
)