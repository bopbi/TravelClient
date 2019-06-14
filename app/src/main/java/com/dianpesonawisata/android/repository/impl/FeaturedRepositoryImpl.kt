package com.dianpesonawisata.android.repository.impl

import com.dianpesonawisata.android.model.TourId
import com.dianpesonawisata.android.repository.FeaturedRepository
import com.dianpesonawisata.android.store.FeaturedStore
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.moshi.JsonAdapter
import io.objectbox.Box
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class FeaturedRepositoryImpl(
    featuredReference: DatabaseReference,
    featuredStoreBox: Box<FeaturedStore>,
    adapter: JsonAdapter<List<TourId>>
) :
    FeaturedRepository {

    private val featuredSubject = BehaviorSubject.create<List<TourId>>()

    init {
        val featuredStores = featuredStoreBox.all
        if (featuredStores.isNotEmpty()) {
            val featuredStore = featuredStores.first()
            if (featuredStore.content.isNotEmpty()) {
                val tourIds: List<TourId>? = adapter.fromJson(featuredStore.content)
                tourIds?.let {
                    featuredSubject.onNext(it)
                }
            }
        }

        featuredReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val featured = dataSnapshot.children.mapNotNull {
                    it.getValue(TourId::class.java)
                }.toList()

                featuredStoreBox.removeAll()
                val newFeaturedStore = FeaturedStore(content = adapter.toJson(featured))
                featuredStoreBox.put(newFeaturedStore)
                featuredSubject.onNext(featured)

            }

        })
    }

    override fun getFeatured(): Observable<List<TourId>> {
        return featuredSubject
    }
}
