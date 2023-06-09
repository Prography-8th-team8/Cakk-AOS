package org.prography.utility.mapper

import org.prography.domain.model.store.StoreModel
import org.prography.network.api.dto.response.StoreReloadResponse
import org.prography.network.api.dto.response.StoreResponse

fun List<StoreResponse>.toModel(): List<StoreModel> = map { it.toModel() }

@JvmName("toStoreModel")
fun List<StoreReloadResponse>.toModel(): List<StoreModel> = map { it.toModel() }
