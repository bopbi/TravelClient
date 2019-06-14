package com.dianpesonawisata.android.domain

import com.dianpesonawisata.android.model.Tour
import io.reactivex.Observable

interface GetTours {

    fun execute(): Observable<List<Tour>>
}