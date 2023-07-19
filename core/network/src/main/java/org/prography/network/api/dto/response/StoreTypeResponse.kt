package org.prography.network.api.dto.response

import kotlinx.serialization.Serializable
import org.prography.domain.model.store.StoreModel

@Serializable
data class StoreTypeResponse(
    val storeId: Int,
    val types: List<String>
) {
    fun toModel() = StoreModel(
        id = storeId,
        storeTypes = types
    )
}
