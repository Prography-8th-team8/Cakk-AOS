package org.prography.feed

import dagger.hilt.android.lifecycle.HiltViewModel
import org.prography.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor() : BaseViewModel<FeedUiAction, FeedUiState, Nothing>(
    initialState = FeedUiState()
) {
    override fun reduceState(currentState: FeedUiState, action: FeedUiAction): FeedUiState {
        return currentState
    }
}
