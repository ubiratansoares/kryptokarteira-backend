package br.ufs.kryptokarteira.backend.services.output

class WalletPayload(
        val owner: String,
        val savings: List<SavingPayload>

)

class SavingPayload(
        val name: String,
        val amount: Float
)
