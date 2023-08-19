package org.prography.home

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreModel

sealed class HomeUiAction : BaseAction {
    object Loading : HomeUiAction()
    data class LoadStoreList(val storeModels: List<StoreModel>) : HomeUiAction()

    data class LoadStoreType(val storeModel: StoreModel) : HomeUiAction()

    data class LoadBookmarkedCakeShop(val id: Int) : HomeUiAction()

    data class BookmarkCakeShop(val id: Int) : HomeUiAction()

    data class UnBookmarkCakeShop(val id: Int) : HomeUiAction()

    data class ReloadStore(val storeModels: List<StoreModel>) : HomeUiAction()

    data class LoadStoreTypes(val storeTypes: String) : HomeUiAction()

    object FilterCakeShop : HomeUiAction()

    object BottomSheetExpandFull : HomeUiAction()

    object BottomSheetExpandQuarter : HomeUiAction()

    object BottomSheetExpandCollapsed : HomeUiAction()

    object BottomSheetExpandHalf : HomeUiAction()

    object BottomSheetStoreList : HomeUiAction()
    data class BottomSheetStoreDetail(val storeId: Int) : HomeUiAction()
    object BottomSheetFilter : HomeUiAction()
}
