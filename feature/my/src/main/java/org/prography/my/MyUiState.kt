package org.prography.my

import org.prography.base.BaseState
import org.prography.domain.model.store.StoreModel

data class MyUiState(
    val bookmarkModels: List<StoreModel> = listOf()
) : BaseState
