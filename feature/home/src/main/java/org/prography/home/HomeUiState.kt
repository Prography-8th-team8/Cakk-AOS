package org.prography.home

import org.prography.base.BaseState

data class HomeUiState(
    val storeModels: List<StoreModel> = listOf(),
) : BaseState
