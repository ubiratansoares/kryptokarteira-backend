package br.ufs.kryptokarteira.backend.services

import com.google.gson.Gson

object OutputMapper {

    private val gson by lazy { Gson() }

    fun asJson(value: Any): String {
        return gson.toJson(value)
    }

}