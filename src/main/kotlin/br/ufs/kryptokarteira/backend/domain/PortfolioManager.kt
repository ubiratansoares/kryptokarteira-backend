package br.ufs.kryptokarteira.backend.domain

interface PortfolioManager {

    fun savings() : List<Investiment>

}