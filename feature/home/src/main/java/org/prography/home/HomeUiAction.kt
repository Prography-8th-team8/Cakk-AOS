package org.prography.home

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreModel

sealed class HomeUiAction : BaseAction {
    data object Loading : HomeUiAction()

    data class LoadStoreList(val storeModels: List<StoreModel>) : HomeUiAction()

    data class LoadStoreType(val storeModel: StoreModel) : HomeUiAction()

    data class LoadBookmarkedCakeShop(val id: Int) : HomeUiAction()

    data class BookmarkCakeShop(val id: Int) : HomeUiAction()

    data class UnBookmarkCakeShop(val id: Int) : HomeUiAction()

    data class ReloadStore(val storeModels: List<StoreModel>) : HomeUiAction()

    data class LoadStoreTypes(val storeTypes: String) : HomeUiAction()

    data class BottomSheetStoreDetail(val storeId: Int) : HomeUiAction()

    data object InitCakeShop : HomeUiAction()

    data object BottomSheetExpandFull : HomeUiAction()

    data object BottomSheetExpandQuarter : HomeUiAction()

    data object BottomSheetExpandCollapsed : HomeUiAction()

    data object BottomSheetExpandHalf : HomeUiAction()

    data object BottomSheetStoreList : HomeUiAction()

    data object BottomSheetFilter : HomeUiAction()
}
