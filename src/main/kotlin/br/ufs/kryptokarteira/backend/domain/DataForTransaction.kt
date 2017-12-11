package br.ufs.kryptokarteira.backend.domain

data class DataForTransaction(
        val owner: String,
        val type: String,
        val currency: Currency,
        val amount: Float
) {
    companion object {
        val BUY_OPERATION = "buy"
        val SELL_OPERATION = "sell"
    }
}