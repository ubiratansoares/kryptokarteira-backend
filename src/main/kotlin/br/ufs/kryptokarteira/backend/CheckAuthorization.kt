package br.ufs.kryptokarteira.backend

import spark.Request

object CheckAuthorization {

    operator fun invoke(request: Request): Boolean {
        val authorization = request.headers("Authorization")
        return KEYS.contains(authorization)
    }

    private val KEYS = listOf("ABCDEF")

}