package com.dianpesonawisata.android.group

import com.dianpesonawisata.android.domain.GetGroups
import com.dianpesonawisata.android.domain.GetTours
import com.dianpesonawisata.android.model.Group
import com.dianpesonawisata.android.model.GroupedTour
import com.dianpesonawisata.android.model.Tour
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class GroupActionProcessor(
    private val getTours: GetTours,
    private val getGroups: GetGroups
) {

    private val loadDataProcess =
        ObservableTransformer<GroupAction.LoadGroupByIdAction, GroupResult.LoadDataResult> { actions ->
            actions.flatMap { action ->
                Observable.combineLatest(
                    getTours.execute(),
                    getGroups.execute(),
                    BiFunction<List<Tour>, List<Group>, GroupResult.LoadDataResult> { tours, groups ->

                        val toursMap = mutableMapOf<Int, Tour>()
                        tours.forEach { tour ->
                            toursMap[tour.id] = tour
                        }

                        val groupedTours = mutableListOf<GroupedTour>()
                        groups.forEach { group ->

                            val tourContent = mutableListOf<Tour>()
                            group.tour_ids.forEach { tourId ->
                                val tour = toursMap[tourId.tour_id]
                                tour?.let {
                                    tourContent.add(tour)
                                }
                            }
                            val groupedTour = GroupedTour(group.id, group.title, group.image_main, tourContent)
                            groupedTours.add(groupedTour)
                        }

                        val groupedTour = groupedTours.findLast { groupedTour ->
                            groupedTour.id == action.id
                        }

                        if (groupedTour == null) {
                            GroupResult.LoadDataResult.Fail(IllegalArgumentException())
                        } else {
                            GroupResult.LoadDataResult.Success(
                                groupedTour = groupedTour
                            )
                        }
                    })
                    .startWith(GroupResult.LoadDataResult.Loading)
                    .onErrorReturn(GroupResult.LoadDataResult::Fail)
            }
        }

    var actionProcessor = ObservableTransformer<GroupAction, GroupResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                listOf(
                    shared.ofType(GroupAction.LoadGroupByIdAction::class.java).compose(loadDataProcess)
                )
            )
                .cast(GroupResult::class.java)
                .mergeWith(
                    shared.filter {
                        it !is GroupAction.LoadGroupByIdAction
                    }.flatMap { w ->
                        Observable.error<GroupResult>(IllegalArgumentException("Unknown Action type: $w"))
                    }
                )
        }
    }
}
