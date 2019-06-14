package com.dianpesonawisata.android.tour

import com.dianpesonawisata.android.model.Tour

sealed class TourResult {

    sealed class LoadDataResult : TourResult() {

        data class Success(
            val tour: Tour
        ) : LoadDataResult()

        object Loading : LoadDataResult()

        data class Fail(val throwable: Throwable) : LoadDataResult()
    }
}
