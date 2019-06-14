package com.dianpesonawisata.android.group

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dianpesonawisata.android.R
import com.dianpesonawisata.android.constant.IntentConstant
import com.dianpesonawisata.android.viewmodel.GroupViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.lifecycle.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class GroupFragment : Fragment() {

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    private var groupId: Int = -1

    private lateinit var viewModel: GroupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        groupId = arguments?.getInt(IntentConstant.GROUPED_TOUR_ID) ?: -1
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, GroupViewModelFactory()).get(GroupViewModel::class.java)
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

    private fun intents(): Observable<GroupIntent> {
        return Observable.merge(listOf(Observable.just(GroupIntent.LoadGroupByIdIntent(groupId))))
    }

    private fun render(state: GroupViewState) {
        state.groupedTour?.let { groupedTour ->
            activity?.title = groupedTour.title

            // Glide.with(tourImage.context).load(groupedTour.image_main).into(tourImage)
        }
    }
}
