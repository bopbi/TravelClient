package com.dianpesonawisata.android.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.diffcallback.GroupedTourDiffCallback
import com.dianpesonawisata.android.model.GroupedTour
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class GroupedTourAdapter :
    ListAdapter<GroupedTour, GroupedTourAdapter.GroupedTourItemViewHolder>(GroupedTourDiffCallback()) {

    private val clickSubject: PublishSubject<GroupedTour> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): GroupedTourItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grouped_tour, parent, false)
        return GroupedTourItemViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: GroupedTourItemViewHolder, position: Int) {
        val groupedTour = currentList[position]
        viewholder.bindTour(groupedTour, clickSubject)
    }

    class GroupedTourItemViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.item_tour_title)
        private val description = view.findViewById<TextView>(R.id.item_tour_desc)
        private val image = view.findViewById<ImageView>(R.id.item_tour_image)
        private lateinit var groupedTour: GroupedTour
        private lateinit var clickSubject: PublishSubject<GroupedTour>

        init {
            view.setOnClickListener {
                clickSubject.onNext(groupedTour)
            }
        }

        fun bindTour(groupedTour: GroupedTour, clickSubject: PublishSubject<GroupedTour>) {
            this.groupedTour = groupedTour
            this.clickSubject = clickSubject
            title.text = groupedTour.title
            description.text = "${groupedTour.tours.size} destinasi"
            Glide.with(image.context).load(groupedTour.image_main).into(image)
        }
    }

    fun getClickObservable(): Observable<GroupedTour> {
        return clickSubject
    }
}
