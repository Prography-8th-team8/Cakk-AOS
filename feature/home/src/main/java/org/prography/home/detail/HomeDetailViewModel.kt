package org.prography.home.detail

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
class HomeDetailViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseViewModel<HomeDetailAction, HomeDetailState, Nothing>(
    initialState = HomeDetailState()
) {
    override fun reduceState(currentState: HomeDetailState, action: HomeDetailAction): HomeDetailState = when (action) {
        HomeDetailAction.Loading -> currentState
        is HomeDetailAction.LoadDetailInfo -> {
            currentState.copy(storeDetailModel = action.storeDetailModel)
        }

        is HomeDetailAction.LoadBlogInfos -> {
            currentState.copy(blogPosts = action.blogPosts)
        }

        is HomeDetailAction.ChangeShowBlogCount -> {
            currentState.copy(showBlogPostCount = action.count)
        }

        is HomeDetailAction.LoadBookmarkedCakeShop -> {
            currentState.copy(
                storeDetailModel = if (currentState.storeDetailModel.id == action.id) {
                    currentState.storeDetailModel.copy(bookmarked = true)
                } else {
                    currentState.storeDetailModel
                }
            )
        }

        is HomeDetailAction.BookmarkCakeShop -> {
            currentState.copy(
                storeDetailModel = if (currentState.storeDetailModel.id == action.id) {
                    currentState.storeDetailModel.copy(bookmarked = true)
                } else {
                    currentState.storeDetailModel
                }
            )
        }

        is HomeDetailAction.UnBookmarkCakeShop -> {
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
    fun fetchDetailStore(id: Int) {
        storeRepository.fetchDetailStore(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadDetailInfo(it)) }
            .flatMapMerge { storeRepository.fetchBookmarkedCakeShop(it.id) }
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { it?.id?.let { id -> sendAction(HomeDetailAction.LoadBookmarkedCakeShop(id)) } }
            .launchIn(viewModelScope)
    }

    fun fetchStoreBlogInfos(id: Int) {
        storeRepository.fetchStoreBlog(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadBlogInfos(it.blogPosts)) }
            .launchIn(viewModelScope)
    }

    fun changeShowBlogCount(count: Int) {
        viewModelScope.launch {
            sendAction(HomeDetailAction.ChangeShowBlogCount(count))
        }
    }

    fun bookmarkCakeShop(bookmarkModel: StoreModel) {
        viewModelScope.launch {
            storeRepository.bookmarkStore(bookmarkModel = bookmarkModel)
            sendAction(HomeDetailAction.BookmarkCakeShop(bookmarkModel.id))
        }
    }

    fun unBookmarkCakeShop(id: Int) {
        viewModelScope.launch {
            storeRepository.unBookmarkStore(id = id)
            sendAction(HomeDetailAction.UnBookmarkCakeShop(id))
        }
    }
}
