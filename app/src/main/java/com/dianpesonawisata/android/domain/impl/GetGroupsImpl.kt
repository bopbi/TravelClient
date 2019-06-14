package com.dianpesonawisata.android.domain.impl

import com.dianpesonawisata.android.domain.GetGroups
import com.dianpesonawisata.android.model.Group
import com.dianpesonawisata.android.repository.GroupRepository
import io.reactivex.Observable

class GetGroupsImpl(private val groupRepository: GroupRepository) : GetGroups {

    override fun execute(): Observable<List<Group>> {
        return groupRepository.getGroups()
    }
}
