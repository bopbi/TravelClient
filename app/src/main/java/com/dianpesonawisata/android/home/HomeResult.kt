package com.dianpesonawisata.android.home

import com.dianpesonawisata.android.model.GroupedTour
import com.dianpesonawisata.android.model.Tour

sealed class HomeResult {

    sealed class LoadDataResult : HomeResult() {

        data class Success(
            val tours: List<Tour>,
            val groupedTours: List<GroupedTour>,
            val featuredTours: List<Tour>
        ) : LoadDataResult()

        object Loading : LoadDataResult()

        data class Fail(val throwable: Throwable) : LoadDataResult()
    }
}
