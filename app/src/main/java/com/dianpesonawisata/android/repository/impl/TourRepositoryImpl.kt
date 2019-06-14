package com.dianpesonawisata.android.repository.impl

import com.dianpesonawisata.android.model.Tour
import com.dianpesonawisata.android.repository.TourRepository
import com.dianpesonawisata.android.store.TourStore
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType
import io.objectbox.Box
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


class TourRepositoryImpl(tourReference: DatabaseReference, tourStoreBox: Box<TourStore>, adapter: JsonAdapter<List<Tour>>) :
    TourRepository {

    private val toursSubject = BehaviorSubject.create<List<Tour>>()

    init {
        val tourStores = tourStoreBox.all
        if (tourStores.isNotEmpty()) {
            val tourStore = tourStores.first()
            if (tourStore.content.isNotEmpty()) {

                val tours: List<Tour>? = adapter.fromJson(tourStore.content)
                tours?.let {
                    toursSubject.onNext(it)
                }
            }
        }

        tourReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val tours = dataSnapshot.children.mapNotNull {
                    it.getValue(Tour::class.java)
                }.toList()
                tourStoreBox.removeAll()

                val newTourStore = TourStore(content = adapter.toJson(tours))
                tourStoreBox.put(newTourStore)
                toursSubject.onNext(tours)
            }

        })

    }

    override fun getTours(): Observable<List<Tour>> = toursSubject

}
