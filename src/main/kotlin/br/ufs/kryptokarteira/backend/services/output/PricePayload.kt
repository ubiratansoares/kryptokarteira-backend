package br.ufs.kryptokarteira.backend.services.output

class PricePayload(
        val label: String,
        val name: String,
        val buy: Float,
        val sell: Float
)