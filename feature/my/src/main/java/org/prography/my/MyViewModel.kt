package org.prography.my

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.prography.base.BaseViewModel
import org.prography.domain.model.store.BookmarkModel
import org.prography.domain.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : BaseViewModel<MyUiAction, MyUiState, Nothing>(
    initialState = MyUiState()
) {
    override fun reduceState(currentState: MyUiState, action: MyUiAction): MyUiState = when (action) {
        MyUiAction.Loading -> currentState
        is MyUiAction.LoadBookmarkedStoreList -> currentState.copy(bookmarkModels = action.bookmarkModels)
    }

    fun fetchBookmarkedList() {
        storeRepository.fetchBookmarks()
            .onEach { sendAction(MyUiAction.LoadBookmarkedStoreList(it)) }
            .onStart { sendAction(MyUiAction.Loading) }
            .launchIn(viewModelScope)
    }

    fun bookmarkCakeShop(bookmarkModel: BookmarkModel) {
        viewModelScope.launch {
            storeRepository.bookmarkStore(bookmarkModel = bookmarkModel.copy(bookmarked = true))
            fetchBookmarkedList()
        }
    }

    fun unBookmarkCakeShop(id: Int) {
        viewModelScope.launch {
            storeRepository.unBookmarkStore(id = id)
            fetchBookmarkedList()
        }
    }
}
