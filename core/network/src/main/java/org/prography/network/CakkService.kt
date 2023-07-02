package org.prography.network

object CakkService {
    const val BASE_URL = BuildConfig.CAKK_BASE_URL

    object Endpoint {
        const val STORE_LIST = "api/store/list"
        const val STORE_DETAIL = "api/store"
        const val DISTRICT_LIST = "api/store/district/count"
        const val STORE_BLOG = "blog"
    }

    object Parameter {
        const val DISTRICT = "district"
        const val PAGE = "page"
    }
}
