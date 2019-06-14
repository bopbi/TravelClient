package com.dianpesonawisata.android.group

sealed class GroupAction {

    data class LoadGroupByIdAction(val id: Int): GroupAction()
}
