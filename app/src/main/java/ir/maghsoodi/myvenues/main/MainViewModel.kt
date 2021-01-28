package ir.maghsoodi.myvenues.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.utils.DispatcherProvider
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val venusFlow: StateFlow<MainRepository.SearchEvent> = repository.venuesFlow

    fun getNearVenues() {
        viewModelScope.launch(dispatchers.io) {
            repository.getNearVenues(35.7730901,51.3866738)
        }
    }

}