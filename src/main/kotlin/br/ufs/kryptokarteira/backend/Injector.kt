package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.AccountManager
import br.ufs.kryptokarteira.backend.domain.CryptoBanker
import br.ufs.kryptokarteira.backend.domain.CryptoCurrencyTrader
import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.infrastructure.AccountInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.BrokerInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.TraderInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.restdb.RestDBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.networking.RestCaller
import br.ufs.kryptokarteira.backend.rest.APIGateway
import br.ufs.kryptokarteira.backend.services.BrokerService
import br.ufs.kryptokarteira.backend.services.HomeService
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
        bind<RestDBDataSource>() with provider { RestDBDataSource(instance()) }

        bind<PricesBroker>() with provider {
            BrokerInfrastructure(
                    bcb = instance(),
                    mbtc = instance()
            )
        }

        bind<AccountManager>() with provider { AccountInfrastructure(instance()) }
        bind<CryptoCurrencyTrader>() with provider { TraderInfrastructure(instance()) }
        bind<CryptoBanker>() with provider { CryptoBanker(instance()) }

        bind<BrokerService>() with provider { BrokerService(instance()) }

        bind<WalletService>() with provider {
            WalletService(
                    banker = instance(),
                    trader = instance(),
                    broker = instance()
            )
        }

        bind<HomeService>() with provider {
            HomeService(
                    broker = instance(),
                    banker = instance()
            )
        }

        bind<APIGateway>() with provider {
            APIGateway(
                    homeService = instance(),
                    brokerService = instance(),
                    walletService = instance()
            )
        }
    }

    val gateway: APIGateway by graph.instance()

}