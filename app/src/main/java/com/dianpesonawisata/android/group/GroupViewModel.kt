package com.dianpesonawisata.android.group

import androidx.lifecycle.ViewModel
import com.dianpesonawisata.android.domain.GetGroups
import com.dianpesonawisata.android.domain.GetTours
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject

class GroupViewModel(
    getTours: GetTours,
    getGroups: GetGroups
) : ViewModel() {

    private val actionProcessor = GroupActionProcessor(getTours, getGroups)
    private val intentSubject: PublishSubject<GroupIntent> = PublishSubject.create()
    private val stateObservable: Observable<GroupViewState> = compose()
    private val intentFilter: ObservableTransformer<GroupIntent, GroupIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge(
                    listOf(
                        shared.ofType(GroupIntent.LoadGroupByIdIntent::class.java).take(1).cast(GroupIntent::class.java)
                    )
                ).mergeWith(
                    shared.filter {
                        it is GroupIntent.LoadGroupByIdIntent
                    }
                )
            }
        }

    fun compose(): Observable<GroupViewState> {
        return intentSubject.compose(intentFilter)
            .map(this::actionFromIntent)
            .compose(actionProcessor.actionProcessor)
            .scan(GroupViewState.idle(), this::reducer)
            .distinctUntilChanged()
            .replay(1)
            .autoConnect(0)
    }

    fun getStates(): Observable<GroupViewState> {
        return stateObservable
    }

    fun processIntents(intents: Observable<GroupIntent>) {
        intents.subscribe(intentSubject)
    }

    private fun actionFromIntent(intent: GroupIntent): GroupAction {
        return when (intent) {
            is GroupIntent.LoadGroupByIdIntent -> {
                GroupAction.LoadGroupByIdAction(intent.id)
            }
        }
    }

    private fun reducer(previousState: GroupViewState, result: GroupResult): GroupViewState {
        return when (result) {
            is GroupResult.LoadDataResult -> {
                when (result) {
                    is GroupResult.LoadDataResult.Success -> {
                        previousState.copy(
                            isLoading = false,
                            groupedTour = result.groupedTour
                        )
                    }
                    is GroupResult.LoadDataResult.Loading -> {
                        previousState.copy(isLoading = true)
                    }
                    is GroupResult.LoadDataResult.Fail -> {
                        previousState.copy(
                            error = result.throwable
                        )
                    }
                }
            }
        }
    }
}
