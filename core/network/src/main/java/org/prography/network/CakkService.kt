package org.prography.network

object CakkService {
    const val BASE_URL = BuildConfig.CAKK_BASE_URL

    object Endpoint {
        const val STORE_LIST = "api/store/list"
        const val STORE_DETAIL = "api/store"
    }

    object Parameter {
        const val DISTRICT = "district"
        const val PAGE = "page"
    }
}