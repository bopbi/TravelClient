package com.dianpesonawisata.android.store

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class FeaturedStore(@Id var id: Long? = null, var content: String)
