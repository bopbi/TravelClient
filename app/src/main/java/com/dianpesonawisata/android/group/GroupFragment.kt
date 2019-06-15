package com.dianpesonawisata.android.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.constant.IntentConstant
import com.dianpesonawisata.android.tour.TourActivity
import com.dianpesonawisata.android.ui.decoration.MarginItemVerticalDecoration
import com.dianpesonawisata.android.ui.decoration.PaletteToolbarRequestListener
import com.dianpesonawisata.android.viewmodel.GroupViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.lifecycle.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_group.*

class GroupFragment : Fragment() {

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    private var groupId: Int = -1

    private lateinit var viewModel: GroupViewModel

    private lateinit var groupTourAdapter: GroupTourAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        groupId = arguments?.getInt(IntentConstant.GROUPED_TOUR_ID) ?: -1
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, GroupViewModelFactory()).get(GroupViewModel::class.java)

        groupTourAdapter = GroupTourAdapter()
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = groupTourAdapter
        recycler.addItemDecoration(
            MarginItemVerticalDecoration(resources.getDimension(R.dimen.space_16dp).toInt())
        )
    }

    override fun onResume() {
        super.onResume()

        viewModel.getStates()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ render(it) },
                { Log.e("GroupFragment", it.message, it) })

        groupTourAdapter.getClickObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({ tour ->
                TourActivity.open(requireActivity(), tour.id)
            },
                { Log.e("GroupFragment", it.message, it) })

        viewModel.processIntents(intents())
    }

    private fun intents(): Observable<GroupIntent> {
        return Observable.merge(listOf(Observable.just(GroupIntent.LoadGroupByIdIntent(groupId))))
    }

    private fun render(state: GroupViewState) {
        state.groupedTour?.let { groupedTour ->
            toolbarLayout.title = groupedTour.title

            groupTourAdapter.submitList(groupedTour.tours)
            Glide.with(tourImage.context)
                .asBitmap()
                .load(groupedTour.image_main)
                .addListener(PaletteToolbarRequestListener(toolbarLayout))
                .into(tourImage)
        }
    }
}
