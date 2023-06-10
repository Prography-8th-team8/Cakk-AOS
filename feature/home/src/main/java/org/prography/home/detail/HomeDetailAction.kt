package org.prography.home.detail

import org.prography.base.BaseAction

sealed class HomeDetailAction : BaseAction {
    object Loading : HomeDetailAction()
    data class LoadDetailInfo(val id: Int) : HomeDetailAction()
    data class LoadedDetailInfo(val storeDetailModel: StoreDetailModel) : HomeDetailAction()
}
