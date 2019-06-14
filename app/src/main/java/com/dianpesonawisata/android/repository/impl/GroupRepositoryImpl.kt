package com.dianpesonawisata.android.repository.impl

import com.dianpesonawisata.android.model.Group
import com.dianpesonawisata.android.repository.GroupRepository
import com.dianpesonawisata.android.store.GroupStore
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.squareup.moshi.JsonAdapter
import io.objectbox.Box
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class GroupRepositoryImpl(
    groupReference: DatabaseReference,
    groupStoreBox: Box<GroupStore>,
    adapter: JsonAdapter<List<Group>>
) :
    GroupRepository {

    private val groupSubject = BehaviorSubject.create<List<Group>>()

    init {
        val groupStores = groupStoreBox.all
        if (groupStores.isNotEmpty()) {
            val groupStore = groupStores.first()
            if (groupStore.content.isNotEmpty()) {
                val tours: List<Group>? = adapter.fromJson(groupStore.content)
                tours?.let {
                    groupSubject.onNext(it)
                }
            }
        }

        groupReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val groups = dataSnapshot.children.mapNotNull {
                    it.getValue(Group::class.java)
                }.toList()

                groupStoreBox.removeAll()
                val newGroupStore = GroupStore(content = adapter.toJson(groups))
                groupStoreBox.put(newGroupStore)
                groupSubject.onNext(groups)
            }

        })
    }

    override fun getGroups(): Observable<List<Group>> {
        return groupSubject
    }
}
