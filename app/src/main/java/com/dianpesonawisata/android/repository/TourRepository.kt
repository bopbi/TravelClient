package com.dianpesonawisata.android.repository

import com.dianpesonawisata.android.model.Tour
import io.reactivex.Observable

interface TourRepository {

    fun getTours(): Observable<List<Tour>>
}