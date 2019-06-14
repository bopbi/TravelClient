package com.dianpesonawisata.android.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.group.GroupActivity
import com.dianpesonawisata.android.tour.TourActivity
import com.dianpesonawisata.android.ui.decoration.MarginItemHorizontalDecoration
import com.dianpesonawisata.android.ui.decoration.MarginItemVerticalDecoration
import com.dianpesonawisata.android.viewmodel.HomeViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.lifecycle.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    private lateinit var viewModel: HomeViewModel
    private lateinit var tourAdapter: TourAdapter
    private lateinit var featuredAdapter: FeaturedAdapter
    private lateinit var groupedAdapter: GroupedTourAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, HomeViewModelFactory()).get(HomeViewModel::class.java)

        featuredAdapter = FeaturedAdapter()
        val featuredLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerFeatured.layoutManager = featuredLayoutManager
        recyclerFeatured.adapter = featuredAdapter
        recyclerFeatured.addItemDecoration(
            MarginItemHorizontalDecoration(resources.getDimension(R.dimen.space_16dp).toInt())
        )
        ViewCompat.setNestedScrollingEnabled(recyclerFeatured, false)

        groupedAdapter = GroupedTourAdapter()
        val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerGroups.layoutManager = horizontalLayoutManager
        recyclerGroups.adapter = groupedAdapter
        recyclerGroups.addItemDecoration(
            MarginItemHorizontalDecoration(resources.getDimension(R.dimen.space_16dp).toInt())
        )
        ViewCompat.setNestedScrollingEnabled(recyclerGroups, false)

        tourAdapter = TourAdapter()
        recyclerTours.layoutManager = LinearLayoutManager(requireContext())
        recyclerTours.adapter = tourAdapter
        recyclerTours.addItemDecoration(
            MarginItemVerticalDecoration(resources.getDimension(R.dimen.space_16dp).toInt())
        )
        ViewCompat.setNestedScrollingEnabled(recyclerTours, false)

    }

    override fun onResume() {
        super.onResume()

        viewModel.getStates()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ render(it) },
                { Log.e("HomeFragment", it.message, it) })

        featuredAdapter.getClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ tour ->
                TourActivity.open(requireActivity(), tour.id)
            },
                { Log.e("HomeFragment", it.message, it) })

        groupedAdapter.getClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ groupedTour ->
                GroupActivity.open(requireActivity(), groupedTour.id)
            },
                { Log.e("HomeFragment", it.message, it) })

        tourAdapter.getClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ tour ->
                TourActivity.open(requireActivity(), tour.id)
            },
                { Log.e("HomeFragment", it.message, it) })

        viewModel.processIntents(intents())
    }

    private fun intents(): Observable<HomeIntent> {
        return Observable.merge(listOf(Observable.just(HomeIntent.LoadDataIntent)))
    }

    private fun render(state: HomeViewState) {
        if (state.isLoading) {
            shimmer.visibility = View.VISIBLE
            layoutContent.visibility = View.GONE
        } else {
            shimmer.visibility = View.GONE
            layoutContent.visibility = View.VISIBLE
        }

        state.featuredTours?.let {
            featuredAdapter.submitList(it)
        }

        state.groupedTours?.let {
            groupedAdapter.submitList(it)
        }

        state.tours?.let {
            tourAdapter.submitList(it)
        }
    }
}
