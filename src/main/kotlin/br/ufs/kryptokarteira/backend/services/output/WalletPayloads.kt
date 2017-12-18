package br.ufs.kryptokarteira.backend.services.output

import br.ufs.kryptokarteira.backend.domain.BankAccount
import br.ufs.kryptokarteira.backend.domain.TransactionLog

class NewWalletCreatedPayload(
        val owner: String
)

class WalletPayload(
        val owner: String,
        val savings: List<SavingPayload>,
        val transactions: List<TransactionLog>
)

class SavingPayload(
        val label: String,
        val amount: Float
)

class TransactionResultPayload(
        val message: String
)

object WalletPayloadFromBankAccount {
    operator fun invoke(account: BankAccount) = with(account) {
        WalletPayload(
                owner = account.owner,
                savings = savings.map { SavingPayload(it.currency.label, it.amount) },
                transactions = history.reversed()
        )
    }
}

object NewWalletCreatedPayloadFromBankAccount {
    operator fun invoke(account: BankAccount) = with(account) {
        NewWalletCreatedPayload(owner = account.owner)
    }
}