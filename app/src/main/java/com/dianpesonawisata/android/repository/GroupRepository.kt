package com.dianpesonawisata.android.repository

import com.dianpesonawisata.android.model.Group
import io.reactivex.Observable

interface GroupRepository {

    fun getGroups(): Observable<List<Group>>
}
