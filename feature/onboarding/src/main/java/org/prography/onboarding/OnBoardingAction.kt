package org.prography.onboarding

import org.prography.base.BaseAction
import org.prography.domain.model.district.DistrictModel

sealed class OnBoardingAction : BaseAction {
    object Loading : OnBoardingAction()
    object LoadDistrictList : OnBoardingAction()

    data class LoadedDistrictList(
        val districts: List<DistrictModel>
    ) : OnBoardingAction()
}
