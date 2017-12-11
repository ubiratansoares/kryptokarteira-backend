package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.KryptoBanker
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.infrastructure.BrokerInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller
import br.ufs.kryptokarteira.backend.rest.APIGateway
import br.ufs.kryptokarteira.backend.services.BrokerService
import br.ufs.kryptokarteira.backend.services.WalletService
import com.github.salomonbrys.kodein.*
import com.google.gson.Gson
import okhttp3.OkHttpClient

object Injector {

    private val graph = Kodein.lazy {

        bind<Gson>() with singleton { Gson() }
        bind<OkHttpClient>() with singleton { OkHttpClient.Builder().build() }

        bind<RestCaller>() with provider { RestCaller(instance()) }
        bind<BCBDataSource>() with provider { BCBDataSource(instance()) }
        bind<MBTCDataSource>() with provider { MBTCDataSource(instance()) }

        bind<PricesBroker>() with provider {
            BrokerInfrastructure(
                    bcb = instance(),
                    mbtc = instance()
            )
        }

        bind<KryptoBanker>() with provider { KryptoBanker() }

        bind<BrokerService>() with provider { BrokerService(instance()) }
        bind<WalletService>() with provider { WalletService(instance()) }

        bind<APIGateway>() with provider {
            APIGateway(
                    brokerService = instance(),
                    walletService = instance()
            )
        }
    }

    val gateway: APIGateway by graph.instance()

}