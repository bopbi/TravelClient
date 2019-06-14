package com.dianpesonawisata.android.group

sealed class GroupIntent {

    data class LoadGroupByIdIntent(val id: Int): GroupIntent()
}
