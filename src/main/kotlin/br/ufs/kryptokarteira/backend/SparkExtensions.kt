package br.ufs.kryptokarteira.backend

import spark.kotlin.ExceptionHandler
import spark.kotlin.RouteHandler

fun RouteHandler.reply(message: String): String {
    type(contentType = "application/json")
    return "{\"message\":\"$message\"}"
}

fun ExceptionHandler.replyWith(statusCode: Int, errorMessage: String) {
    status(statusCode)
    type(contentType = "application/json")
    response.body("{\"message\":\"$errorMessage\"}")
}