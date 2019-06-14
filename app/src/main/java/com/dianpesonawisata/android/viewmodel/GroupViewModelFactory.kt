package com.dianpesonawisata.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dianpesonawisata.android.dependency.RepositorySingleton
import com.dianpesonawisata.android.domain.GetGroups
import com.dianpesonawisata.android.domain.GetTours
import com.dianpesonawisata.android.domain.impl.GetGroupsImpl
import com.dianpesonawisata.android.domain.impl.GetToursImpl
import com.dianpesonawisata.android.group.GroupViewModel

@Suppress("UNCHECKED_CAST")
class GroupViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val tourRepository = RepositorySingleton.tourRepository
        val getTours: GetTours = GetToursImpl(tourRepository)
        val getGroups: GetGroups = GetGroupsImpl(RepositorySingleton.groupRepository)
        return GroupViewModel(getTours, getGroups) as T
    }

}
