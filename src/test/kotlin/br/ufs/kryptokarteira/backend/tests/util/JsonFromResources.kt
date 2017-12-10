package br.ufs.kryptokarteira.backend.tests.util

import com.google.gson.Gson
import java.io.File
import java.util.*
import kotlin.reflect.KClass

object JsonFromResources {

    private val GSON = Gson()

    operator fun <T : Any> invoke(fileName: String, kClass: KClass<T>): T {

        val result = StringBuilder()
        val classLoader = JsonFromResources::class.java.classLoader
        val file = File(classLoader.getResource(fileName).file)

        try {
            val scanner = Scanner(file)
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                result.append(line).append("\n")
            }

            scanner.close()
            val json = result.toString()

            return GSON.fromJson(json, kClass.java) as T

        } catch (e: Throwable) {
            e.printStackTrace()
        }

        throw RuntimeException("Cannot read file" + fileName)
    }

}