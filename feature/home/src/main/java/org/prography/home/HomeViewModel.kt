package org.prography.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.prography.base.BaseViewModel
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.repository.store.StoreRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseViewModel<HomeUiAction, HomeUiState, HomeUiSideEffect>(
    initialState = HomeUiState()
) {
    override fun reduceState(currentState: HomeUiState, action: HomeUiAction): HomeUiState = when (action) {
        HomeUiAction.Loading -> currentState
        is HomeUiAction.LoadStoreList -> {
            fetchStoreList(action.districts)
            currentState
        }
        is HomeUiAction.LoadedStoreList -> {
            currentState.copy(storeModels = currentState.storeModels + action.storeModels)
        }
    }

    @OptIn(FlowPreview::class)
    private fun fetchStoreList(districts: List<String>) {
        Timber.i("fetchStoreList ${districts.first()}")
        districts.asFlow()
            .flatMapMerge { storeRepository.fetchStoreList(StoreListRequest(it, 1)) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadedStoreList(it.toModel())) }
            .launchIn(viewModelScope)
    }
}
