package org.prography.utility.mapper

import org.prography.domain.model.store.StoreModel
import org.prography.network.api.dto.response.StoreListResponse

fun List<StoreListResponse>.toModel(): List<StoreModel> = map { it.toModel() }
