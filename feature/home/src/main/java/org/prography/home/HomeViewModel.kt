package org.prography.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.prography.base.BaseViewModel
import org.prography.domain.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
) : BaseViewModel<HomeUiAction, HomeUiState, Nothing>(
    initialState = HomeUiState()
) {
    override fun reduceState(currentState: HomeUiState, action: HomeUiAction): HomeUiState = when (action) {
        HomeUiAction.BottomSheetExpandFull -> currentState.copy(lastExpandedType = ExpandedType.FULL)
        HomeUiAction.BottomSheetExpandQuarter -> currentState.copy(lastExpandedType = ExpandedType.QUARTER)
        HomeUiAction.BottomSheetExpandCollapsed -> currentState.copy(lastExpandedType = ExpandedType.COLLAPSED)
        HomeUiAction.BottomSheetExpandHalf -> currentState.copy(lastExpandedType = ExpandedType.HALF)
        HomeUiAction.Loading -> currentState
        is HomeUiAction.LoadStoreList -> {
            fetchStoreList(action.districts, action.storeTypes)
            currentState
        }
        is HomeUiAction.LoadedStoreList -> {
            currentState.copy(storeModels = currentState.storeModels + action.storeModels)
        }
    }

    @OptIn(FlowPreview::class)
    private fun fetchStoreList(districts: List<String>, storeTypes: String) {
        districts.sorted().asFlow()
            .flatMapMerge { storeRepository.fetchStoreList(it, storeTypes, 1) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadedStoreList(it)) }
            .launchIn(viewModelScope)
    }
}
