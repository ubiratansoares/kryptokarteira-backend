package br.ufs.kryptokarteira.backend.services.output

import br.ufs.kryptokarteira.backend.domain.BankAccount
import br.ufs.kryptokarteira.backend.domain.TransactionLog

class WalletPayload(
        val owner: String,
        val savings: List<SavingPayload>,
        val transactions: List<TransactionLog>
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
                savings = savings.map { SavingPayload(it.currency.name, it.amount) },
                transactions = history.reversed()
        )
    }
}