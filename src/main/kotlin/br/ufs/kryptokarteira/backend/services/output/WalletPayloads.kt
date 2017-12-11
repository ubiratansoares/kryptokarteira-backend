package br.ufs.kryptokarteira.backend.services.output

import br.ufs.kryptokarteira.backend.domain.BankAccount

class WalletPayload(
        val owner: String,
        val savings: List<SavingPayload>
)

class SavingPayload(
        val name: String,
        val amount: Float
)

class TransactionResultPayload(
        val message: String
)

object WalletPayloadFromBankAccount {
    operator fun invoke(account: BankAccount) = with(account) {
        WalletPayload(
                owner = account.owner,
                savings = savings.map { SavingPayload(it.currency.name, it.amount) }
        )
    }
}