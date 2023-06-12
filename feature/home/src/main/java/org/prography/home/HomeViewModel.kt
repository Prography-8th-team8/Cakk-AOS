package org.prography.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.repository.store.StoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    storeRepository: StoreRepository,
) : ViewModel() {

    val stores = storeRepository.fetchStoreList(StoreListRequest("JONGNO", 1)).stateIn(
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(),
        scope = viewModelScope
    )
}
