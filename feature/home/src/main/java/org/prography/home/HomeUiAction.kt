package org.prography.home

import org.prography.base.BaseAction

sealed class HomeUiAction : BaseAction {
    object Loading : HomeUiAction()

    data class LoadStoreList(val districts: List<String>) : HomeUiAction()

    data class LoadedStoreList(val storeModels: List<StoreModel>) : HomeUiAction()
}
