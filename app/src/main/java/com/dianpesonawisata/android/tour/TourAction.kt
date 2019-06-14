package com.dianpesonawisata.android.tour

sealed class TourAction {

    data class LoadTourByIdAction(val id: Int): TourAction()
}
