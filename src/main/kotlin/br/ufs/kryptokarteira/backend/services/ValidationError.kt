package br.ufs.kryptokarteira.backend.services

sealed class ValidationError : Exception()
class MissingTransactionBody : ValidationError()
class CannotDeserializeJson : ValidationError()
class MissingTransactionType : ValidationError()
class InvalidTransactionType : ValidationError()
class MissingAmount : ValidationError()
class InvalidAmount : ValidationError()
class MissingCurrency : ValidationError()
class InvalidCurrency : ValidationError()