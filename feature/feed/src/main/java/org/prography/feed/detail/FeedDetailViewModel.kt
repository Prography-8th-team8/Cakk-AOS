package org.prography.feed.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.prography.base.BaseViewModel
import org.prography.domain.model.store.StoreModel
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
            is FeedDetailAction.LoadDetailInfo -> currentState.copy(storeDetailModel = action.storeDetailModel)
            is FeedDetailAction.LoadBookmarkedCakeShop -> {
                currentState.copy(
                    storeDetailModel = if (currentState.storeDetailModel.id == action.id) {
                        currentState.storeDetailModel.copy(bookmarked = true)
                    } else {
                        currentState.storeDetailModel
                    }
                )
            }
            is FeedDetailAction.BookmarkCakeShop -> {
                currentState.copy(
                    storeDetailModel = if (currentState.storeDetailModel.id == action.id) {
                        currentState.storeDetailModel.copy(bookmarked = true)
                    } else {
                        currentState.storeDetailModel
                    }
                )
            }
            is FeedDetailAction.UnBookmarkCakeShop -> {
                currentState.copy(
                    storeDetailModel = if (currentState.storeDetailModel.id == action.id) {
                        currentState.storeDetailModel.copy(bookmarked = false)
                    } else {
                        currentState.storeDetailModel
                    }
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchStoreDetailInfo(storeId: Int) {
        storeRepository.fetchDetailStore(storeId)
            .onStart { sendAction(FeedDetailAction.Loading) }
            .onEach { sendAction(FeedDetailAction.LoadDetailInfo(it)) }
            .flatMapMerge { storeRepository.fetchBookmarkedCakeShop(it.id) }
            .onStart { sendAction(FeedDetailAction.Loading) }
            .onEach { it?.let { bookmarkedCakeShop -> sendAction(FeedDetailAction.LoadBookmarkedCakeShop(bookmarkedCakeShop.id)) } }
            .launchIn(viewModelScope)
    }

    fun bookmarkCakeShop(bookmarkModel: StoreModel) {
        viewModelScope.launch {
            storeRepository.bookmarkStore(bookmarkModel = bookmarkModel)
            sendAction(FeedDetailAction.BookmarkCakeShop(bookmarkModel.id))
        }
    }

    fun unBookmarkCakeShop(id: Int) {
        viewModelScope.launch {
            storeRepository.unBookmarkStore(id = id)
            sendAction(FeedDetailAction.UnBookmarkCakeShop(id))
        }
    }
}
