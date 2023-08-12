package org.prography.my

import org.prography.base.BaseState
import org.prography.domain.model.store.BookmarkModel

data class MyUiState(
    val bookmarkModels: List<BookmarkModel> = listOf()
) : BaseState
