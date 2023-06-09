package org.prography.home

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreModel

sealed class HomeUiAction : BaseAction {
    object Loading : HomeUiAction()

    data class LoadStoreList(val districts: List<String>) : HomeUiAction()

    data class LoadedStoreList(val storeModels: List<StoreModel>) : HomeUiAction()

    data class ReloadStore(val storeModels: List<StoreModel>) : HomeUiAction()

    object BottomSheetExpandFull : HomeUiAction()

    object BottomSheetExpandHalf : HomeUiAction()

    object BottomSheetExpandCollapsed : HomeUiAction()
}
