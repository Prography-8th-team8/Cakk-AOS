package org.prography.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        is HomeUiAction.LoadedStoreType -> {
            currentState.copy(
                storeModels = currentState.storeModels.map {
                    if (it.id == action.storeModel.id) {
                        it.copy(storeTypes = action.storeModel.storeTypes)
                    } else {
                        it
                    }
                }
            )
        }
        is HomeUiAction.LoadedStoreList -> {
            currentState.copy(storeModels = currentState.storeModels + action.storeModels, isReload = false)
        }
        is HomeUiAction.ReloadStore -> {
            currentState.copy(storeModels = action.storeModels, isReload = true)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchStoreList(districts: List<String>, storeTypes: String) {
        districts.sorted().asFlow()
            .flatMapMerge { storeRepository.fetchStoreList(it, storeTypes, 1) }
            .onEach { sendAction(HomeUiAction.LoadedStoreList(it)) }
            .flatMapMerge { it.asFlow() }
            .flatMapMerge { storeRepository.fetchStoreType(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadedStoreType(it)) }
            .launchIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
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
            .onEach { sendAction(HomeUiAction.ReloadStore(it)) }
            .flatMapMerge { it.asFlow() }
            .flatMapMerge { storeRepository.fetchStoreType(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadedStoreType(it)) }
            .launchIn(viewModelScope)
    }
}
