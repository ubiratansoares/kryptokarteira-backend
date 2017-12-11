package br.ufs.kryptokarteira.backend.services.output

class PricesPayload(
        val label: String,
        val name: String,
        val buy: Float,
        val sell: Float
)