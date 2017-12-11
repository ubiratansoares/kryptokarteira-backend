package br.ufs.kryptokarteira.backend.services.input

class NewTransactionBody(
        val type: String?,
        val currency: String?,
        val amount: Float?
)