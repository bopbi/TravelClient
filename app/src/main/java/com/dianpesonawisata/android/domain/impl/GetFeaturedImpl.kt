package com.dianpesonawisata.android.domain.impl

import com.dianpesonawisata.android.domain.GetFeatured
import com.dianpesonawisata.android.model.TourId
import com.dianpesonawisata.android.repository.FeaturedRepository
import io.reactivex.Observable

class GetFeaturedImpl(private val featuredRepository: FeaturedRepository) : GetFeatured {

    override fun execute(): Observable<List<TourId>> {
        return featuredRepository.getFeatured()
    }
}
