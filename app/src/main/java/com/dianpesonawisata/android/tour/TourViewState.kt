package com.dianpesonawisata.android.tour

import com.dianpesonawisata.android.model.Tour

data class TourViewState(
    val isLoading: Boolean,
    val tour: Tour?,
    val error: Throwable?
) {

    companion object {

        fun idle(): TourViewState {
            return TourViewState(
                isLoading = false,
                tour = null,
                error = null
            )
        }
    }
}
