package com.dianpesonawisata.android.tour

import androidx.lifecycle.ViewModel
import com.dianpesonawisata.android.domain.GetTours
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class TourViewModel(
    getTours: GetTours
) : ViewModel() {

    private val actionProcessor = TourActionProcessor(getTours)
    private val intentSubject: PublishSubject<TourIntent> = PublishSubject.create()
    private val stateObservable: Observable<TourViewState> = compose()
    private val intentFilter: ObservableTransformer<TourIntent, TourIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    listOf(
                        shared.ofType(TourIntent.LoadTourByIdIntent::class.java).take(1).cast(TourIntent::class.java)
                    )
                ).mergeWith(
                    shared.filter {
                        it is TourIntent.LoadTourByIdIntent
                    }
                )
            }
        }

    fun compose(): Observable<TourViewState> {
        return intentSubject.compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(TourViewState.idle(), this::reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    fun getStates(): Observable<TourViewState> {
        return stateObservable
    }

    fun processIntents(intents: Observable<TourIntent>) {
        intents.subscribe(intentSubject)
    }

    private fun actionFromIntent(intent: TourIntent): TourAction {
        return when (intent) {
            is TourIntent.LoadTourByIdIntent -> {
                TourAction.LoadTourByIdAction(intent.id)
            }
        }
    }

    private fun reducer(previousState: TourViewState, result: TourResult): TourViewState {
        return when (result) {
            is TourResult.LoadDataResult -> {
                when (result) {
                    is TourResult.LoadDataResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            tour = result.tour
                        )
                    }
                    is TourResult.LoadDataResult.Loading -> {
                        previousState.copy(isLoading = true)
                    }
                    is TourResult.LoadDataResult.Fail -> {
                        previousState.copy(
                            error = result.throwable
                        )
                    }
                }
            }
        }
    }
}
