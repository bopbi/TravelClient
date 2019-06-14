package com.dianpesonawisata.android.dependency

import com.dianpesonawisata.android.model.Group
import com.dianpesonawisata.android.model.Tour
import com.dianpesonawisata.android.model.TourId
import com.dianpesonawisata.android.repository.FeaturedRepository
import com.dianpesonawisata.android.repository.GroupRepository
import com.dianpesonawisata.android.repository.TourRepository
import com.dianpesonawisata.android.repository.impl.FeaturedRepositoryImpl
import com.dianpesonawisata.android.repository.impl.GroupRepositoryImpl
import com.dianpesonawisata.android.repository.impl.TourRepositoryImpl
import com.dianpesonawisata.android.store.FeaturedStore
import com.dianpesonawisata.android.store.GroupStore
import com.dianpesonawisata.android.store.ObjectBox
import com.dianpesonawisata.android.store.TourStore
import com.google.firebase.database.FirebaseDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object RepositorySingleton {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val toursDatabaseReference = FirebaseDatabase.getInstance().getReference("tours")
    private val tourBox = ObjectBox.get().boxFor(TourStore::class.java)
    private val toursType = Types.newParameterizedType(List::class.java, Tour::class.java)
    private val toursAdapter = moshi.adapter<List<Tour>>(toursType)
    val tourRepository: TourRepository = TourRepositoryImpl(toursDatabaseReference, tourBox, toursAdapter)

    private val groupBox = ObjectBox.get().boxFor(GroupStore::class.java)
    private val groupsDatabaseReference = FirebaseDatabase.getInstance().getReference("groups")
    private val groupType = Types.newParameterizedType(List::class.java, Group::class.java)
    private val groupsAdapter = moshi.adapter<List<Group>>(groupType)
    val groupRepository: GroupRepository = GroupRepositoryImpl(groupsDatabaseReference, groupBox, groupsAdapter)

    private val featuredBox = ObjectBox.get().boxFor(FeaturedStore::class.java)
    private val featuredDatabaseReference = FirebaseDatabase.getInstance().getReference("featured")
    private val tourIdType = Types.newParameterizedType(List::class.java, TourId::class.java)
    private val tourIdsAdapter = moshi.adapter<List<TourId>>(tourIdType)
    val featuredRepository: FeaturedRepository = FeaturedRepositoryImpl(featuredDatabaseReference, featuredBox, tourIdsAdapter)
}
