package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.infrastructure.BrokerInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.RestCaller
import br.ufs.kryptokarteira.backend.infrastructure.datasources.bcb.BCBDataSource
import br.ufs.kryptokarteira.backend.infrastructure.datasources.mbtc.MBTCDataSource
import br.ufs.kryptokarteira.backend.rest.APIGateway
import br.ufs.kryptokarteira.backend.services.BrokerService
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

        bind<PricesBroker>() with provider { BrokerInfrastructure(instance(), instance()) }
        bind<BrokerService>() with provider { BrokerService(instance()) }

        bind<APIGateway>() with provider { APIGateway(instance()) }
    }

    val gateway: APIGateway by graph.instance()

}