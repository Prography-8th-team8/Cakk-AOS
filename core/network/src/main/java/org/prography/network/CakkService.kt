package org.prography.network

object CakkService {
    const val BASE_URL = BuildConfig.CAKK_BASE_URL

    object Endpoint {
        const val STORE_LIST = "api/store/list"
        const val STORE_DETAIL = "api/store"
        const val DISTRICT_LIST = "api/store/district/count"
        const val STORE_BLOG = "blog"
        const val STORE_RELOAD = "api/store/reload"
        const val STORE_TYPE = "type"
    }

    object Parameter {
        const val DISTRICT = "district"
        const val STORETYPE = "storeTypes"
        const val PAGE = "page"
        const val STORE_TYPES = "storeTypes"
        const val SOUTH_WEST_LATITUDE = "southwestLatitude"
        const val SOUTH_WEST_LONGITUDE = "southwestLongitude"
        const val NORTH_EAST_LATITUDE = "northeastLatitude"
        const val NORTH_EAST_LONGITUDE = "northeastLongitude"
    }
}
