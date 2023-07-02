package org.prography.home.detail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.prography.base.BaseViewModel
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
            fetchDetailStore(action.id)
            currentState
        }
        is HomeDetailAction.LoadedDetailInfo -> {
            currentState.copy(storeDetailModel = action.storeDetailModel)
        }
        is HomeDetailAction.LoadBlogInfos -> {
            fetchStoreBlogInfos(action.id)
            currentState
        }
        is HomeDetailAction.LoadedBlogInfos -> {
            currentState.copy(blogPosts = action.blogPosts)
        }
    }

    private fun fetchDetailStore(id: Int) {
        storeRepository.fetchDetailStore(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadedDetailInfo(it)) }
            .launchIn(viewModelScope)
    }

    private fun fetchStoreBlogInfos(id: Int) {
        storeRepository.fetchStoreBlog(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadedBlogInfos(it.blogPosts)) }
            .launchIn(viewModelScope)
    }
}
