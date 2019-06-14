package com.dianpesonawisata.android.tour

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.constant.IntentConstant.Companion.TOUR_ID
import com.dianpesonawisata.android.viewmodel.TourViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.lifecycle.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_tour.*

class TourFragment : Fragment() {

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    private var tourId: Int = -1

    private lateinit var viewModel: TourViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        tourId = arguments?.getInt(TOUR_ID) ?: -1
        return inflater.inflate(R.layout.fragment_tour, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, TourViewModelFactory()).get(TourViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        viewModel.getStates()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ render(it) },
                { Log.e("TourFragment", it.message, it) })

        viewModel.processIntents(intents())
    }

    private fun intents(): Observable<TourIntent> {
        return Observable.merge(listOf(Observable.just(TourIntent.LoadTourByIdIntent(tourId))))
    }

    private fun render(state: TourViewState) {
        state.tour?.let { tour ->
            activity?.title = tour.title
            tourTitle.text = tour.title
            tourDesc.text = tour.desc
            Glide.with(tourImage.context).load(tour.image_main).into(tourImage)
        }
    }
}
