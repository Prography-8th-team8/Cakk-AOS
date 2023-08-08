package org.prography.feed

import org.prography.base.BaseState
import org.prography.domain.model.store.FeedModel

data class FeedUiState(
    private val feedModels: List<FeedModel> = listOf()
) : BaseState
