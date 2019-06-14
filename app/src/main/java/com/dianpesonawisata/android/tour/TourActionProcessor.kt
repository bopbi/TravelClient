package com.dianpesonawisata.android.tour

import com.dianpesonawisata.android.domain.GetTours
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class TourActionProcessor(
    private val getTours: GetTours
) {

    private val loadDataProcess =
        ObservableTransformer<TourAction.LoadTourByIdAction, TourResult.LoadDataResult> { actions ->
            actions.flatMap { action ->
                getTours.execute().map { tours ->
                    val tour = tours.findLast { tour ->
                        action.id == tour.id
                    }

                    if (tour == null) {
                        TourResult.LoadDataResult.Fail(IllegalArgumentException())
                    } else {
                        TourResult.LoadDataResult.Success(
                            tour = tour
                        )
                    }

                }
                    .startWith(TourResult.LoadDataResult.Loading)
                    .onErrorReturn(TourResult.LoadDataResult::Fail)
            }
        }

    var actionProcessor = ObservableTransformer<TourAction, TourResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                listOf(
                    shared.ofType(TourAction.LoadTourByIdAction::class.java).compose(loadDataProcess)
                )
            )
                .cast(TourResult::class.java)
                .mergeWith(
                    shared.filter {
                        it !is TourAction.LoadTourByIdAction
                    }.flatMap { w ->
                        Observable.error<TourResult>(IllegalArgumentException("Unknown Action type: $w"))
                    }
                )
        }
    }
}
