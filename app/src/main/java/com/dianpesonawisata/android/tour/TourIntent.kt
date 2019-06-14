package com.dianpesonawisata.android.tour

sealed class TourIntent {

    data class LoadTourByIdIntent(val id: Int): TourIntent()
}
