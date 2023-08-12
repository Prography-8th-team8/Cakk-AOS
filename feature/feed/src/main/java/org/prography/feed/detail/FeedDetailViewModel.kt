package org.prography.feed.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.prography.base.BaseViewModel
import org.prography.domain.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class FeedDetailViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : BaseViewModel<FeedDetailAction, FeedDetailState, Nothing>(
    initialState = FeedDetailState()
) {
    override fun reduceState(currentState: FeedDetailState, action: FeedDetailAction): FeedDetailState =
        when (action) {
            FeedDetailAction.Loading -> currentState
            is FeedDetailAction.LoadDetailInfo -> currentState.copy(action.storeDetailModel)
        }

    fun fetchStoreDetailInfo(storeId: Int) {
        storeRepository.fetchDetailStore(storeId)
            .onStart { sendAction(FeedDetailAction.Loading) }
            .onEach { sendAction(FeedDetailAction.LoadDetailInfo(it)) }
            .launchIn(viewModelScope)
    }
}
