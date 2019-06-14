package com.dianpesonawisata.android.domain.impl

import com.dianpesonawisata.android.domain.GetTours
import com.dianpesonawisata.android.model.Tour
import com.dianpesonawisata.android.repository.TourRepository
import io.reactivex.Observable

class GetToursImpl(private val tourRepository: TourRepository): GetTours {

    override fun execute(): Observable<List<Tour>> {
        return tourRepository.getTours()
    }

}