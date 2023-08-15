package org.prography.my

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreModel

sealed class MyUiAction : BaseAction {

    object Loading : MyUiAction()

    data class LoadBookmarkedStoreList(val bookmarkModels: List<StoreModel>) : MyUiAction()
}
