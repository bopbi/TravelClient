package com.dianpesonawisata.android.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.dianpesonawisata.android.model.Tour

class TourDiffCallback : DiffUtil.ItemCallback<Tour>() {

    override fun areItemsTheSame(oldItem: Tour, newItem: Tour): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Tour, newItem: Tour): Boolean {
        return oldItem == newItem
    }

}
