package com.dianpesonawisata.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dianpesonawisata.android.dependency.RepositorySingleton
import com.dianpesonawisata.android.domain.GetTours
import com.dianpesonawisata.android.domain.impl.GetToursImpl
import com.dianpesonawisata.android.tour.TourViewModel

@Suppress("UNCHECKED_CAST")
class TourViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val tourRepository = RepositorySingleton.tourRepository
        val getTours: GetTours = GetToursImpl(tourRepository)
        return TourViewModel(getTours) as T
    }

}
