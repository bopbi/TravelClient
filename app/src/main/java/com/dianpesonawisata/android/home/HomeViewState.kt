package com.dianpesonawisata.android.home

import com.dianpesonawisata.android.model.GroupedTour
import com.dianpesonawisata.android.model.Tour

data class HomeViewState(
    val isLoading: Boolean,
    val tours: List<Tour>?,
    val featuredTours: List<Tour>?,
    val groupedTours: List<GroupedTour>?,
    val error: Throwable?
) {

    companion object {

        fun idle(): HomeViewState {
            return HomeViewState(
                isLoading = false,
                tours = null,
                featuredTours = null,
                groupedTours = null,
                error = null
            )
        }
    }
}
