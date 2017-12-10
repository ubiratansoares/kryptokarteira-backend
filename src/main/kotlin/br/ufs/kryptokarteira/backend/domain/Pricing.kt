package br.ufs.kryptokarteira.backend.domain

data class Pricing(
        val currency: Currency,
        val buyPrice: Float,
        val sellPrice: Float
)
