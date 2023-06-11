package org.prography.onboarding

import org.prography.base.BaseState

data class OnBoardingState(
    val districtList: List<DistrictModel> = listOf(),
) : BaseState
