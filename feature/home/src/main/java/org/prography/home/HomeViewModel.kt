package org.prography.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.prography.base.BaseViewModel
import org.prography.cakk.data.api.model.request.StoreListRequest
import org.prography.cakk.data.repository.store.StoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseViewModel<HomeUiAction, HomeUiState, HomeUiSideEffect>(
    initialState = HomeUiState()
) {
    override fun reduceState(currentState: HomeUiState, action: HomeUiAction): HomeUiState = when (action) {
        HomeUiAction.BottomSheetExpandFull -> currentState.copy(lastExpandedType = ExpandedType.FULL)
        HomeUiAction.BottomSheetExpandHalf -> currentState.copy(lastExpandedType = ExpandedType.HALF)
        HomeUiAction.BottomSheetExpandCollapsed -> currentState.copy(lastExpandedType = ExpandedType.COLLAPSED)
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
        districts.sorted().asFlow()
            .flatMapMerge { storeRepository.fetchStoreList(StoreListRequest(it, 1)) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadedStoreList(it.toModel())) }
            .launchIn(viewModelScope)
    }
}
