package ir.maghsoodi.myvenues.main.repository

import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.main.facadePattern.FacadePatternRepository
import ir.maghsoodi.myvenues.utils.Constants.Companion.EXPIRE_TIME_RANGE
import ir.maghsoodi.myvenues.utils.Resource
import ir.maghsoodi.myvenues.utils.TimeManagement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainRepository @Inject constructor(
    val facadeRepository: FacadePatternRepository,
    val timeManagement: TimeManagement
) {

    sealed class SearchEvent {
        class Success(val venueEntities: List<VenueEntity>) : SearchEvent()
        class Failure(val errorText: String) : SearchEvent()
        object Loading : SearchEvent()
        object Empty : SearchEvent()
    }

    private val _venuesFlow = MutableStateFlow<SearchEvent>(SearchEvent.Empty)
    val venuesFlow: StateFlow<SearchEvent> = _venuesFlow

    suspend fun getNearVenues(lat: Double, lng: Double) {
        _venuesFlow.value = SearchEvent.Loading
        getDataFromDb(lat, lng)
        getDataFromNet(lat, lng)

    }

    private suspend fun getDataFromDb(lat: Double, lng: Double) {
        facadeRepository.deleteOldMeta(timeManagement.getCurrentUnixTime() - EXPIRE_TIME_RANGE)
        if (facadeRepository.hasNearestMetaInDB(lat, lng)) {
            val metaEntity = facadeRepository.getNearestMetaInDB(lat, lng)
            val venueEntities = facadeRepository.getVenueEntitiesOfMeta(metaEntity)
            _venuesFlow.value = SearchEvent.Success(venueEntities)
        }
    }

    suspend fun getDataFromNet(lat: Double, lng: Double) {
        when (val searchResponse = facadeRepository.searchNearVenueFromRemote(lat, lng)) {
            is Resource.Error -> _venuesFlow.value = SearchEvent.Failure(searchResponse.message!!)
            is Resource.Success -> {
                _venuesFlow.value = SearchEvent.Success(
                    searchResponse.data!!.response!!.venues
                )
                saveToDB(searchResponse,lat,lng)
            }
        }
    }

    suspend fun saveToDB(ratesResponse: Resource<SearchResponse>,lat: Double, lng: Double) {
        facadeRepository.run {
            saveMetaEntityIntoDB(ratesResponse.data!!.meta,lat,lng)
            saveVenueEntityIntoDB(
                ratesResponse.data.meta,
                ratesResponse.data.response.venues
            )
        }
    }
}