package br.ufs.kryptokarteira.backend.domain


interface PricesBroker {

    fun lastestPrices(): List<Pricing>

}