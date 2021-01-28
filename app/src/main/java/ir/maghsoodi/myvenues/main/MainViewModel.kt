package ir.maghsoodi.myvenues.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.utils.DispatcherProvider
import ir.maghsoodi.myvenues.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val venusFlow: StateFlow<MainRepository.SearchEvent> = repository.venuesFlow

    fun getNearVenues(lat: Double, lng: Double) {
        viewModelScope.launch(dispatchers.io) {
            repository.getNearVenues(lat, lng)
        }
    }

}