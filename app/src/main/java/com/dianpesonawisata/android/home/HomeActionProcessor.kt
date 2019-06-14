package com.dianpesonawisata.android.home

import com.dianpesonawisata.android.domain.GetFeatured
import com.dianpesonawisata.android.domain.GetGroups
import com.dianpesonawisata.android.domain.GetTours
import com.dianpesonawisata.android.model.Group
import com.dianpesonawisata.android.model.GroupedTour
import com.dianpesonawisata.android.model.Tour
import com.dianpesonawisata.android.model.TourId
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function3

class HomeActionProcessor(
    private val getTours: GetTours,
    private val getFeatured: GetFeatured,
    private val getGroups: GetGroups
) {

    private val loadDataProcess =
        ObservableTransformer<HomeAction.LoadDataAction, HomeResult.LoadDataResult> { actions ->
            actions.flatMap {
                Observable.combineLatest(
                    getTours.execute(),
                    getFeatured.execute(),
                    getGroups.execute(),
                    Function3<List<Tour>, List<TourId>, List<Group>, HomeResult.LoadDataResult> { tours, featuredTourIds, groups ->

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

                        val featuredTours = mutableListOf<Tour>()
                        featuredTourIds.forEach { tourId ->
                            val tour = toursMap[tourId.tour_id]
                            tour?.let {
                                featuredTours.add(tour)
                            }
                        }

                        HomeResult.LoadDataResult.Success(
                            tours = tours,
                            groupedTours = groupedTours,
                            featuredTours = featuredTours
                        )
                    })
                    .startWith(HomeResult.LoadDataResult.Loading)
                    .onErrorReturn(HomeResult.LoadDataResult::Fail)
            }
        }

    var actionProcessor = ObservableTransformer<HomeAction, HomeResult> { actions ->
        actions.publish { shared ->
            Observable.merge(
                listOf(
                    shared.ofType(HomeAction.LoadDataAction::class.java).compose(loadDataProcess)
                )
            )
                .cast(HomeResult::class.java)
                .mergeWith(
                    shared.filter {
                        it !is HomeAction.LoadDataAction
                    }.flatMap { w ->
                        Observable.error<HomeResult>(IllegalArgumentException("Unknown Action type: $w"))
                    }
                )
        }
    }
}
