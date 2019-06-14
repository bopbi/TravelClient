package com.dianpesonawisata.android.group

import com.dianpesonawisata.android.model.GroupedTour

sealed class GroupResult {

    sealed class LoadDataResult : GroupResult() {

        data class Success(
            val groupedTour: GroupedTour
        ) : LoadDataResult()

        object Loading : LoadDataResult()

        data class Fail(val throwable: Throwable) : LoadDataResult()
    }
}
