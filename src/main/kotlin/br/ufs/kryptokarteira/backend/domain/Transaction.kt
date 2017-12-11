package br.ufs.kryptokarteira.backend.domain

sealed class Transaction {

    class Successfull : Transaction()
    class Invalid : Transaction()
    class Failed : Transaction()

}