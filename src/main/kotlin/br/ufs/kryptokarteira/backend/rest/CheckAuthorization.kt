package br.ufs.kryptokarteira.backend.rest

import br.ufs.kryptokarteira.backend.Configs
import spark.Request

object CheckAuthorization {

    private val authKey by lazy { Configs.fromFileOrEnv("KRYPTOKARTEIRA_AUTHKEY") }

    operator fun invoke(request: Request): Boolean {
        val authorization = request.headers("Authorization")
        return authorization == authKey
    }

}