package br.ufs.kryptokarteira.backend.rest

import spark.HaltException
import spark.Spark
import spark.kotlin.ExceptionHandler
import spark.kotlin.RouteHandler

fun RouteHandler.reply(func: () -> Reply): String {
    val reply = func()
    status(reply.code)
    type(contentType = "application/json")
    return "{\"message\":\"${reply.message}\"}"
}

fun ExceptionHandler.failure(func: () -> Reply) {
    val reply = func()
    status(reply.code)
    type(contentType = "application/json")
    response.body("{\"error\":\"${reply.message}\"}")
}

infix fun RouteHandler.deny(func: () -> Reply): HaltException {
    val reply = func()
    status(reply.code)
    type(contentType = "application/json")
    val body = "{\"error\":\"${reply.message}\"}"
    return Spark.halt(reply.code, body)
}