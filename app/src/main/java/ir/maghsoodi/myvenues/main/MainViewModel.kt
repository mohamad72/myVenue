package ir.maghsoodi.myvenues.main

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.utils.Constants.Companion.MAXIMUM_NEAR_DISTANCE
import ir.maghsoodi.myvenues.utils.Constants.Companion.NUMBER_OF_ITEMS_IN_EACH_PAGE
import ir.maghsoodi.myvenues.utils.DispatcherProvider
import ir.maghsoodi.myvenues.utils.Resource
import ir.maghsoodi.myvenues.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.lang.Exception
import kotlin.math.min


class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    //    val venusFlow: StateFlow<MainRepository.SearchEvent> = repository.venuesFlow
    private val _venuesFlow =
        MutableStateFlow<MainRepository.SearchEvent>(MainRepository.SearchEvent.Empty)
    val venuesFlow: StateFlow<MainRepository.SearchEvent> = _venuesFlow

    var venueEntities: List<VenueEntity> = emptyList()

    var lastLat: Double = 0.0
    var lastLng: Double = 0.0

    var pageNumberOfList = 1

    fun getNearVenuesMore() {
        _venuesFlow.value = MainRepository.SearchEvent.Loading
        pageNumberOfList++
        viewModelScope.launch(dispatchers.default) {
            delay(1500)
            _venuesFlow.value =
                MainRepository.SearchEvent.Success(
                    venueEntities.subList(
                        0,
                        min(
                            NUMBER_OF_ITEMS_IN_EACH_PAGE * pageNumberOfList,
                            venueEntities.size
                        )
                    )
                )
        }
    }

    fun getNearVenues(lat: Double, lng: Double) {
        Timber.tag("location").d("location changed again $lat, $lng")
        if (isLocationReallyChanged(lat, lng)) {
            pageNumberOfList = 1
            lastLat = lat
            lastLng = lng
            viewModelScope.launch(dispatchers.main) {
                repository.venuesFlow.collect { event ->
                    Timber.tag("observer_viewe").d(event.toString())
                    when (event) {
                        is MainRepository.SearchEvent.Success -> {
                            venueEntities = event.venueEntities
                            _venuesFlow.value =
                                MainRepository.SearchEvent.Success(
                                    event.venueEntities.subList(
                                        0,
                                        min(
                                            NUMBER_OF_ITEMS_IN_EACH_PAGE * pageNumberOfList,
                                            event.venueEntities.size
                                        )
                                    )
                                )
                            Timber.tag("loadData").e("failed updateVenueList ")
                        }
                        else -> _venuesFlow.value = event
                    }
                }
            }
            viewModelScope.launch(dispatchers.io) {
                repository.getNearVenues(lastLat, lastLng)
            }
        }
    }

    fun isLocationReallyChanged(lat: Double, lng: Double): Boolean = (lastLat == 0.0 ||
            lastLng == 0.0 ||
            Utils.calculateDistance(lat, lng, lastLat, lastLng) > MAXIMUM_NEAR_DISTANCE)


}