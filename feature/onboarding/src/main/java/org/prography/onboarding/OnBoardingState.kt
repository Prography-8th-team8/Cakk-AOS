package org.prography.onboarding

import org.prography.base.BaseState
import org.prography.domain.model.district.DistrictGroupModel

data class OnBoardingState(
    val districtGroups: List<DistrictGroupModel> = listOf(),
) : BaseState
