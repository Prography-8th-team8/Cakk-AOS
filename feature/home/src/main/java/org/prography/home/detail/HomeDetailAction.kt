package org.prography.home.detail

import org.prography.base.BaseAction
import org.prography.domain.model.store.StoreDetailModel

sealed class HomeDetailAction : BaseAction {
    object Loading : HomeDetailAction()
    data class LoadDetailInfo(val id: Int) : HomeDetailAction()
    data class LoadedDetailInfo(val storeDetailModel: StoreDetailModel) : HomeDetailAction()
}
