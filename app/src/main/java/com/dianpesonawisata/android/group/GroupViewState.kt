package com.dianpesonawisata.android.group

import com.dianpesonawisata.android.model.GroupedTour

data class GroupViewState(
    val isLoading: Boolean,
    val groupedTour: GroupedTour?,
    val error: Throwable?
) {

    companion object {

        fun idle(): GroupViewState {
            return GroupViewState(
                isLoading = false,
                groupedTour = null,
                error = null
            )
        }
    }
}
