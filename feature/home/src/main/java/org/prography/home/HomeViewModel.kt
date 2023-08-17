package org.prography.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.prography.base.BaseViewModel
import org.prography.domain.model.store.StoreModel
import org.prography.domain.repository.FilterRepository
import org.prography.domain.repository.StoreRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository,
    private val filterRepository: FilterRepository,
) : BaseViewModel<HomeUiAction, HomeUiState, HomeSideEffect>(
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

        is HomeUiAction.LoadBookmarkedCakeShop -> {
            currentState.copy(
                storeModels = currentState.storeModels.map {
                    if (it.id == action.id) {
                        it.copy(bookmarked = true)
                    } else {
                        it
                    }
                }
            )
        }

        is HomeUiAction.BookmarkCakeShop -> {
            currentState.copy(
                storeModels = currentState.storeModels.map {
                    if (it.id == action.id) {
                        it.copy(bookmarked = true)
                    } else {
                        it
                    }
                }
            )
        }

        is HomeUiAction.UnBookmarkCakeShop -> {
            currentState.copy(
                storeModels = currentState.storeModels.map {
                    if (it.id == action.id) {
                        it.copy(bookmarked = false)
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

        is HomeUiAction.LoadStoreTypes -> {
            currentState.copy(storeTypes = action.storeTypes)
        }
    }

    init {
        getStoreTypes()
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
            BottomSheetType.Filter -> sendAction(HomeUiAction.BottomSheetFilter)
            is BottomSheetType.StoreDetail -> sendAction(HomeUiAction.BottomSheetStoreDetail(bottomSheetType.storeId))
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
            .flatMapMerge { storeRepository.fetchBookmarkedCakeShop(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { it?.let { bookmarkedCakeShop -> sendAction(HomeUiAction.LoadBookmarkedCakeShop(bookmarkedCakeShop.id)) } }
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
            .catch {
                sendSideEffect(HomeSideEffect.ReloadError)
                sendAction(HomeUiAction.LoadStoreList(listOf()))
            }
            .flatMapMerge { it.asFlow() }
            .flatMapMerge { storeRepository.fetchStoreType(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { sendAction(HomeUiAction.LoadStoreType(it)) }
            .flatMapMerge { storeRepository.fetchBookmarkedCakeShop(it.id) }
            .onStart { sendAction(HomeUiAction.Loading) }
            .onEach { it?.let { bookmarkedCakeShop -> sendAction(HomeUiAction.LoadBookmarkedCakeShop(bookmarkedCakeShop.id)) } }
            .launchIn(viewModelScope)
    }

    fun bookmarkCakeShop(bookmarkModel: StoreModel) {
        viewModelScope.launch {
            storeRepository.bookmarkStore(bookmarkModel = bookmarkModel)
            sendAction(HomeUiAction.BookmarkCakeShop(bookmarkModel.id))
        }
    }

    fun unBookmarkCakeShop(id: Int) {
        viewModelScope.launch {
            storeRepository.unBookmarkStore(id = id)
            sendAction(HomeUiAction.UnBookmarkCakeShop(id))
        }
    }

    private fun getStoreTypes() {
        filterRepository.fetchFilters()
            .onStart { HomeUiAction.Loading }
            .onEach { sendAction(HomeUiAction.LoadStoreTypes(it)) }
            .launchIn(viewModelScope)
    }

    fun saveStoreTypes(
        southwestLatitude: Double? = null,
        southwestLongitude: Double? = null,
        northeastLatitude: Double? = null,
        northeastLongitude: Double? = null,
        districts: List<String> = listOf(),
        storeTypes: String,
        clickLocationChange: Boolean
    ) {
        viewModelScope.launch {
            filterRepository.saveFilters(storeTypes)
            sendAction(HomeUiAction.LoadStoreTypes(storeTypes))
            if (clickLocationChange) {
                fetchStoreReload(
                    southwestLatitude,
                    southwestLongitude,
                    northeastLatitude,
                    northeastLongitude,
                    storeTypes = storeTypes.split(",").filter { it != "" }
                )
            } else {
                fetchStoreList(districts, storeTypes)
            }
            sendSideEffect(HomeSideEffect.FilterCakeShop)
        }
    }
}
