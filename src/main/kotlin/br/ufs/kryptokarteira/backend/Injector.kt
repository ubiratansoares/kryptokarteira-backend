package br.ufs.kryptokarteira.backend

import br.ufs.kryptokarteira.backend.domain.PricesBroker
import br.ufs.kryptokarteira.backend.infrastructure.PricingInfrastructure
import br.ufs.kryptokarteira.backend.infrastructure.RestCaller
import br.ufs.kryptokarteira.backend.services.PricingService
import com.github.salomonbrys.kodein.*
import com.google.gson.Gson
import okhttp3.OkHttpClient

object Injector {

    private val graph = Kodein.lazy {

        bind<Gson>() with singleton { Gson() }
        bind<OkHttpClient>() with singleton { OkHttpClient.Builder().build() }
        bind<RestCaller>() with provider { RestCaller(instance()) }

        bind<PricesBroker>() with provider { PricingInfrastructure(instance()) }
        bind<PricingService>() with provider { PricingService(instance()) }

        bind<APIGateway>() with provider { APIGateway(instance()) }
    }

    val gateway: APIGateway by graph.instance()

}