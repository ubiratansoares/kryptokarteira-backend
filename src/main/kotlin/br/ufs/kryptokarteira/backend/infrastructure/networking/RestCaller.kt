package br.ufs.kryptokarteira.backend.infrastructure.networking

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import kotlin.reflect.KClass

class RestCaller(private val httpClient: OkHttpClient) {

    private val gson by lazy { Gson() }

    fun <T : Any> get(
            url: String,
            output: KClass<T>,
            header: Header = Header.IGNORE): T {

        val builder = Request.Builder().url(url)
        if (header != Header.IGNORE) builder.addHeader(header.name, header.value)
        val request = builder.build()
        return executeAndAssemble(request, output)
    }


    fun <T : Any> post(
            url: String,
            jsonBody: String,
            authorization: Header,
            output: KClass<T>): T {

        val body = createBody(jsonBody)

        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader(authorization.name, authorization.value)
                .build()

        return executeAndAssemble(request, output)

    }

    fun <T : Any> put(
            url: String,
            jsonBody: String,
            authorization: Header,
            output: KClass<T>): T {

        val body = createBody(jsonBody)

        val request = Request.Builder()
                .url(url)
                .put(body)
                .addHeader(authorization.name, authorization.value)
                .build()

        return executeAndAssemble(request, output)
    }

    private fun createBody(jsonBody: String): RequestBody {
        return RequestBody.create(MediaType.parse("application/json"), jsonBody)
    }

    private fun <T : Any> executeAndAssemble(request: Request, target: KClass<T>): T {
        val response = httpClient.newCall(request).execute()

        if (response.isSuccessful) {
            val result = response.body()?.string()
            return gson.fromJson(result, target.java) as T
        }

        when (response.code()) {
            in 400..499 -> throw RestIntegrationError.ClientError
            else -> throw RestIntegrationError.InternalServerError
        }
    }
}