package com.dianpesonawisata.android.model

data class GroupedTour(val id: Int, val title: String, val image_main: String = "", val tours: List<Tour>)
