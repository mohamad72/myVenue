package ir.maghsoodi.myvenues.main

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.utils.Constants.Companion.MAXIMUM_NEAR_DISTANCE
import ir.maghsoodi.myvenues.utils.DispatcherProvider
import ir.maghsoodi.myvenues.utils.Resource
import ir.maghsoodi.myvenues.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val venusFlow: StateFlow<MainRepository.SearchEvent> = repository.venuesFlow

    var lastLat: Double = 0.0
    var lastLng: Double = 0.0


    fun getNearVenues(lat: Double, lng: Double) {
        Timber.tag("location").d("location changed again $lat, $lng")
        if (lastLat == 0.0 ||
            lastLng == 0.0 ||
            Utils.calculateDistance(lat, lng, lastLat, lastLng) > MAXIMUM_NEAR_DISTANCE
        )
        {
            lastLat=lat
            lastLng=lng
            viewModelScope.launch(dispatchers.io) {
                repository.getNearVenues(lat, lng)
            }
        }
    }


}