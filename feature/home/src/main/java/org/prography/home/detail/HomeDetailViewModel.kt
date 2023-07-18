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
            currentState.copy(storeDetailModel = action.storeDetailModel)
        }
        is HomeDetailAction.LoadBlogInfos -> {
            currentState.copy(blogPosts = action.blogPosts)
        }
    }

    fun fetchDetailStore(id: Int) {
        storeRepository.fetchDetailStore(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadDetailInfo(it)) }
            .launchIn(viewModelScope)
    }

    fun fetchStoreBlogInfos(id: Int) {
        storeRepository.fetchStoreBlog(id)
            .onStart { sendAction(HomeDetailAction.Loading) }
            .onEach { sendAction(HomeDetailAction.LoadBlogInfos(it.blogPosts)) }
            .launchIn(viewModelScope)
    }
}
