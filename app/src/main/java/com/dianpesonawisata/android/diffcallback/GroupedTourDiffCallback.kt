package com.dianpesonawisata.android.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.dianpesonawisata.android.model.Group
import com.dianpesonawisata.android.model.GroupedTour
import com.dianpesonawisata.android.model.Tour

class GroupedTourDiffCallback : DiffUtil.ItemCallback<GroupedTour>() {

    override fun areItemsTheSame(oldItem: GroupedTour, newItem: GroupedTour): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GroupedTour, newItem: GroupedTour): Boolean {
        return oldItem == newItem
    }

}
