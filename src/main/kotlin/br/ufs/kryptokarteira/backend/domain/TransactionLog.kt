package br.ufs.kryptokarteira.backend.domain

class TransactionLog (
        val type: String,
        val currency: String,
        val amount: Float,
        val timestamp: String
)