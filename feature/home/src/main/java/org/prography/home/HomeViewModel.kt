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
        HomeUiAction.BottomSheetExpandHalf -> currentState.copy(lastExpandedType = ExpandedType.HALF)
        HomeUiAction.BottomSheetExpandCollapsed -> currentState.copy(lastExpandedType = ExpandedType.COLLAPSED)
        HomeUiAction.Loading -> currentState
        is HomeUiAction.LoadStoreList -> {
            fetchStoreList(action.districts)
            currentState
        }
        is HomeUiAction.LoadedStoreList -> {
            currentState.copy(storeModels = currentState.storeModels + action.storeModels, isReload = false)
        }
        is HomeUiAction.ReloadStore -> {
            currentState.copy(storeModels = action.storeModels, isReload = true)
        }
    }

    @OptIn(FlowPreview::class)
    private fun fetchStoreList(districts: List<String>) {
        districts.sorted().asFlow()
            .flatMapMerge { storeRepository.fetchStoreList(it, 1) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadedStoreList(it)) }
            .launchIn(viewModelScope)
    }

    fun fetchStoreReload(
        southwestLatitude: Double?,
        southwestLongitude: Double?,
        northeastLatitude: Double?,
        northeastLongitude: Double?,
        storeTypes: List<String> = listOf()
    ) {
        requireNotNull(southwestLatitude)
        requireNotNull(southwestLongitude)
        requireNotNull(northeastLatitude)
        requireNotNull(northeastLongitude)

        storeRepository.fetchStoreReload(
            southwestLatitude,
            southwestLongitude,
            northeastLatitude,
            northeastLongitude,
            storeTypes = storeTypes
        )
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.ReloadStore(it)) }
            .launchIn(viewModelScope)
    }
}
