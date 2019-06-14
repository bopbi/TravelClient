package com.dianpesonawisata.android.model

data class Group(
    val id: Int = -1,
    val title: String = "",
    val image_main: String = "",
    val tour_ids: List<TourId> = emptyList()
)
