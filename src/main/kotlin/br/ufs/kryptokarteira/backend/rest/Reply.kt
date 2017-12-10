package br.ufs.kryptokarteira.backend.rest

data class Reply(
        val code: Int,
        val message: String
)

infix fun Int.withMessage(that: String) = Reply(this, that)
