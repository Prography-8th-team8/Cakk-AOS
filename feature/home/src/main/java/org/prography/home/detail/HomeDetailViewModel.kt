package org.prography.home.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.prography.base.BaseViewModel
import org.prography.cakk.data.repository.store.StoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeDetailViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseViewModel<HomeDetailAction, HomeDetailState, HomeDetailSideEffect>(
    initialState = HomeDetailState()
) {
    override fun reduceState(currentState: HomeDetailState, action: HomeDetailAction): HomeDetailState = when (action) {
        HomeDetailAction.Loading -> currentState
        is HomeDetailAction.LoadDetailInfo -> {
            fetchDetailStore(action.id)
            currentState
        }
        is HomeDetailAction.LoadedDetailInfo -> currentState.copy(storeDetailModel = action.storeDetailModel)
    }

    private fun fetchDetailStore(id: Int) {
        storeRepository.fetchDetailStore(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadedDetailInfo(it.toModel())) }
            .launchIn(viewModelScope)
    }
}
