package br.ufs.kryptokarteira.backend.services.util

import com.google.gson.Gson
import kotlin.reflect.KClass

object JsonSerializer {

    private val gson by lazy { Gson() }

    fun asJson(value: Any): String {
        return gson.toJson(value)
    }

    fun <T : Any> fromJson(value: String, target: KClass<T>): T {
        try {
            return gson.fromJson(value, target.java) as T
        } catch (error: Throwable) {
            throw CannotDeserializeJson()
        }
    }

}