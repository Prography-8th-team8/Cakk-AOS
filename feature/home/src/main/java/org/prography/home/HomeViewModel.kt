package org.prography.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.api.model.response.StoreListResponse
import org.prography.cakk.data.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : ViewModel() {

    private val _stores = MutableStateFlow<List<StoreListResponse>?>(null)
    val stores = _stores.asStateFlow()

    fun fetchStoreList(districtType: String = "JONGNO", page: Int = 1) {
        viewModelScope.launch {
            runCatching {
                storeRepository.fetchStoreList(StoreListRequest(districtType, page))
            }.onSuccess {
                _stores.value = it
            }.onFailure {
                it.message
            }
        }
    }
}
