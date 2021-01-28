package ir.maghsoodi.myvenues.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import ir.maghsoodi.myvenues.data.models.Location
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.main.repository.MainRepository
import ir.maghsoodi.myvenues.utils.DispatcherProvider
import ir.maghsoodi.myvenues.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class VenueDetailModel @ViewModelInject constructor() : ViewModel() {

    fun getVenueEntity(venueEntityString: String?): VenueEntity {
        return Gson().fromJson(venueEntityString, VenueEntity::class.java)
    }

    fun getFullAddress(venueEntity: VenueEntity): String {
        return venueEntity.location.getFullAddress()
    }
}