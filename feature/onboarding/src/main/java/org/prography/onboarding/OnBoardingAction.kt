package org.prography.onboarding

import org.prography.base.BaseAction
import org.prography.domain.model.district.DistrictModel

sealed class OnBoardingAction : BaseAction {
    object Loading : OnBoardingAction()

    data class LoadDistrictList(
        val districts: List<DistrictModel>
    ) : OnBoardingAction()
}
