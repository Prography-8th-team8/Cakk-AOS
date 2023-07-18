package org.prography.onboarding

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.prography.base.BaseViewModel
import org.prography.domain.repository.DistrictRepository
import org.prography.utility.mapper.toGroup
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val districtRepository: DistrictRepository
) : BaseViewModel<OnBoardingAction, OnBoardingState, Nothing>(
    initialState = OnBoardingState()
) {
    override fun reduceState(currentState: OnBoardingState, action: OnBoardingAction): OnBoardingState = when (action) {
        OnBoardingAction.Loading -> currentState
        is OnBoardingAction.LoadDistrictList -> {
            currentState.copy(districtGroups = action.districts.toGroup())
        }
    }

    fun fetchDistrictList() {
        districtRepository.fetchDistrictList()
            .onStart { sendAction(OnBoardingAction.Loading) }
            .onEach { sendAction(OnBoardingAction.LoadDistrictList(it)) }
            .launchIn(viewModelScope)
    }
}
