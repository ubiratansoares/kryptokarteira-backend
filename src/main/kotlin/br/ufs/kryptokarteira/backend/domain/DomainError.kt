package br.ufs.kryptokarteira.backend.domain

sealed class DomainError : Exception()
class ExternalServiceUnavailable : DomainError()
class ExternalServiceTimeout : DomainError()
class ExternalServiceIntegrationError : DomainError()
class ExternalServiceContractError : DomainError()
class UnknownInternalError : DomainError()
class CannotPerformTransaction : DomainError()