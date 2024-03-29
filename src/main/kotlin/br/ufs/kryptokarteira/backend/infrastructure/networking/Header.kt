package br.ufs.kryptokarteira.backend.infrastructure.networking

data class Header(val name: String, val value: String) {

    companion object {
        val IGNORE = Header("ignore", "ignore")
    }
}