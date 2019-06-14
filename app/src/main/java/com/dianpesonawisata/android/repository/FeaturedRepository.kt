package com.dianpesonawisata.android.repository

import com.dianpesonawisata.android.model.TourId
import io.reactivex.Observable

interface FeaturedRepository {

    fun getFeatured(): Observable<List<TourId>>
}
