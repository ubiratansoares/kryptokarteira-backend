package br.ufs.kryptokarteira.backend.infrastructure

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.reflect.KClass

class RestCaller(private val httpClient: OkHttpClient) {

    private val gson by lazy { Gson() }

    fun <T : Any> get(url: String, target: KClass<T>): T {
        val request = Request.Builder()
                .url(url)
                .build()

        val response = httpClient.newCall(request).execute()
        val result = response.body()?.string()

        return gson.fromJson(result, target.java) as T
    }

}