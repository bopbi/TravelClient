package com.dianpesonawisata.android.domain

import com.dianpesonawisata.android.model.TourId
import io.reactivex.Observable

interface GetFeatured {

    fun execute(): Observable<List<TourId>>
}
