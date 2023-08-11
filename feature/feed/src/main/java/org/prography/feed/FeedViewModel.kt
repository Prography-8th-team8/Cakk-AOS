package org.prography.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.prography.domain.model.store.FeedModel
import org.prography.domain.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    storeRepository: StoreRepository
) : ViewModel() {
    val feedItems: Flow<PagingData<FeedModel>> = storeRepository
        .fetchStoreFeed()
        .cachedIn(viewModelScope)
}
