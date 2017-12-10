package br.ufs.kryptokarteira.backend

import spark.HaltException
import spark.Spark
import spark.kotlin.ExceptionHandler
import spark.kotlin.RouteHandler

fun RouteHandler.reply(statusCode: Int, message: String): String {
    status(statusCode)
    type(contentType = "application/json")
    return "{\"message\":\"$message\"}"
}

fun ExceptionHandler.replyWithError(statusCode: Int, errorMessage: String) {
    status(statusCode)
    type(contentType = "application/json")
    response.body("{\"message\":\"$errorMessage\"}")
}

fun RouteHandler.deny(statusCode: Int, message: String): HaltException {
    status(statusCode)
    type(contentType = "application/json")
    val body = "{\"message\":\"$message\"}"
    return Spark.halt(statusCode, body)
}