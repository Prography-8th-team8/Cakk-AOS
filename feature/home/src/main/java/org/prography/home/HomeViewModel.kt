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
        HomeUiAction.Loading -> currentState
        HomeUiAction.BottomSheetExpandFull -> currentState.copy(expandedType = ExpandedType.FULL)
        HomeUiAction.BottomSheetExpandQuarter -> currentState.copy(expandedType = ExpandedType.QUARTER)
        HomeUiAction.BottomSheetExpandCollapsed -> currentState.copy(expandedType = ExpandedType.COLLAPSED)
        HomeUiAction.BottomSheetExpandHalf -> currentState.copy(expandedType = ExpandedType.HALF)
        HomeUiAction.BottomSheetFilter -> currentState.copy(bottomSheetType = BottomSheetType.Filter)
        HomeUiAction.BottomSheetStoreList -> currentState.copy(bottomSheetType = BottomSheetType.StoreList)
        is HomeUiAction.BottomSheetStoreDetail -> currentState.copy(bottomSheetType = BottomSheetType.StoreDetail(action.storeId))
        is HomeUiAction.LoadStoreType -> {
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

        is HomeUiAction.LoadStoreList -> {
            currentState.copy(storeModels = currentState.storeModels + action.storeModels, isReload = false)
        }

        is HomeUiAction.ReloadStore -> {
            currentState.copy(storeModels = action.storeModels, isReload = true)
        }
    }

    fun changeBottomSheetState(expandedType: ExpandedType) {
        when (expandedType) {
            ExpandedType.FULL -> sendAction(HomeUiAction.BottomSheetExpandFull)
            ExpandedType.QUARTER -> sendAction(HomeUiAction.BottomSheetExpandQuarter)
            ExpandedType.HALF -> sendAction(HomeUiAction.BottomSheetExpandHalf)
            ExpandedType.COLLAPSED -> sendAction(HomeUiAction.BottomSheetExpandCollapsed)
            else -> return
        }
    }

    fun changeBottomSheetType(bottomSheetType: BottomSheetType) {
        when (bottomSheetType) {
            BottomSheetType.StoreList -> sendAction(HomeUiAction.BottomSheetStoreList)
            is BottomSheetType.StoreDetail -> sendAction(HomeUiAction.BottomSheetStoreDetail(bottomSheetType.storeId))
            BottomSheetType.Filter -> sendAction(HomeUiAction.BottomSheetFilter)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchStoreList(districts: List<String>, storeTypes: String) {
        districts.sorted().asFlow()
            .flatMapMerge { storeRepository.fetchStoreList(it, storeTypes, 1) }
            .onEach { sendAction(HomeUiAction.LoadStoreList(it)) }
            .flatMapMerge { it.asFlow() }
            .flatMapMerge { storeRepository.fetchStoreType(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadStoreType(it)) }
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
            .catch { sendAction(HomeUiAction.LoadStoreList(listOf())) }
            .flatMapMerge { it.asFlow() }
            .flatMapMerge { storeRepository.fetchStoreType(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadStoreType(it)) }
            .launchIn(viewModelScope)
    }
}
