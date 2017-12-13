package br.ufs.kryptokarteira.backend

import java.util.*

object Configs {

    private val all by lazy { readFile() }

    fun fromFileOrEnv(key: String): String {
        val assignedOrNot = ProcessBuilder().environment()[key]
        return assignedOrNot?.let { it } ?: Configs.all.getProperty(key)
    }

    private fun readFile(): Properties {
        // This file does not below to VCS
        val classLoader = Configs::class.java.classLoader
        val propFileName = "private/config.properties"
        val inputStream = classLoader.getResourceAsStream(propFileName)

        try {
            val properties = Properties()
            properties.load(inputStream)
            return properties
        } catch (e: Exception) {
            throw RuntimeException("Cannot read file config.properties")
        } finally {
            inputStream.close()
        }
    }

}