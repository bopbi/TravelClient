package com.dianpesonawisata.android.domain

import com.dianpesonawisata.android.model.Group
import io.reactivex.Observable

interface GetGroups {

    fun execute(): Observable<List<Group>>
}
