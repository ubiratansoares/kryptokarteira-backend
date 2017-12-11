package br.ufs.kryptokarteira.backend.services

import br.ufs.kryptokarteira.backend.domain.BankAccount
import br.ufs.kryptokarteira.backend.domain.KryptoBanker
import br.ufs.kryptokarteira.backend.services.output.SavingPayload
import br.ufs.kryptokarteira.backend.services.output.WalletPayload
import br.ufs.kryptokarteira.backend.services.util.OutputMapper
import br.ufs.kryptokarteira.backend.services.util.ServiceOperation

class WalletService(private val banker: KryptoBanker) {

    private val mapper = OutputMapper

    fun newWallet(): ServiceOperation {
        val account = banker.newAccount()
        val output = walletPayload(account)
        return ServiceOperation(200, mapper.asJson(output))
    }

    private fun walletPayload(account: BankAccount): WalletPayload {
        return with(account, {
            WalletPayload(
                    owner,
                    savings.map { SavingPayload(it.currency.name, it.amount) }
            )
        })
    }

}