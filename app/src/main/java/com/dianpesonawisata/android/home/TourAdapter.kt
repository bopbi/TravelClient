package com.dianpesonawisata.android.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.diffcallback.TourDiffCallback
import com.dianpesonawisata.android.model.Tour
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class TourAdapter : ListAdapter<Tour, TourAdapter.TourItemViewHolder>(TourDiffCallback()) {

    private val clickSubject: PublishSubject<Tour> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TourItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tour, parent, false)
        return TourItemViewHolder(view)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(viewholder: TourItemViewHolder, position: Int) {
        val tour = currentList[position]
        viewholder.bindTour(tour, clickSubject)
    }

    class TourItemViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.item_tour_title)
        private val description = view.findViewById<TextView>(R.id.item_tour_desc)
        private val image = view.findViewById<ImageView>(R.id.item_tour_image)
        private lateinit var tour: Tour
        private lateinit var clickSubject: PublishSubject<Tour>

        init {
            view.setOnClickListener {
                clickSubject.onNext(tour)
            }
        }

        fun bindTour(tour: Tour, clickSubject: PublishSubject<Tour>) {
            this.tour = tour
            this.clickSubject = clickSubject
            title.text = tour.title
            description.text = tour.desc
            Glide.with(image.context).load(tour.image_main).into(image)
        }
    }

    fun getClickObservable(): Observable<Tour> {
        return clickSubject
    }
}
