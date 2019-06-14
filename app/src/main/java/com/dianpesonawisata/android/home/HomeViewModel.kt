package com.dianpesonawisata.android.home

import androidx.lifecycle.ViewModel
import com.dianpesonawisata.android.domain.GetFeatured
import com.dianpesonawisata.android.domain.GetGroups
import com.dianpesonawisata.android.domain.GetTours
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class HomeViewModel(
    getTours: GetTours,
    getFeatured: GetFeatured,
    getGroups: GetGroups
) : ViewModel() {

    private val actionProcessor = HomeActionProcessor(getTours, getFeatured, getGroups)
    private val intentSubject: PublishSubject<HomeIntent> = PublishSubject.create()
    private val stateObservable: Observable<HomeViewState> = compose()
    private val intentFilter: ObservableTransformer<HomeIntent, HomeIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    listOf(
                        shared.ofType(HomeIntent.LoadDataIntent::class.java).take(1).cast(HomeIntent::class.java)
                    )
                ).mergeWith(
                    shared.filter {
                        it is HomeIntent.LoadDataIntent
                    }
                )
            }
        }

    fun compose(): Observable<HomeViewState> {
        return intentSubject.compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(HomeViewState.idle(), this::reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    fun getStates(): Observable<HomeViewState> {
        return stateObservable
    }

    fun processIntents(intents: Observable<HomeIntent>) {
        intents.subscribe(intentSubject)
    }

    private fun actionFromIntent(intent: HomeIntent): HomeAction {
        return when (intent) {
            is HomeIntent.LoadDataIntent -> {
                HomeAction.LoadDataAction
            }
        }
    }

    private fun reducer(previousState: HomeViewState, result: HomeResult): HomeViewState {
        return when (result) {
            is HomeResult.LoadDataResult -> {
                when (result) {
                    is HomeResult.LoadDataResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            tours = result.tours,
                            featuredTours = result.featuredTours,
                            groupedTours = result.groupedTours
                        )
                    }
                    is HomeResult.LoadDataResult.Loading -> {
                        previousState.copy(isLoading = true)
                    }
                    is HomeResult.LoadDataResult.Fail -> {
                        previousState.copy(
                            error = result.throwable
                        )
                    }
                }
            }
        }
    }
}
