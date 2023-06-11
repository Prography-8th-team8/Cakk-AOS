package org.prography.onboarding

import org.prography.base.BaseState

data class OnBoardingState(
    val districtGroups: List<DistrictGroupModel> = listOf(),
) : BaseState
