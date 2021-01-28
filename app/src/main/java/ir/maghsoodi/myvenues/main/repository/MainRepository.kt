package ir.maghsoodi.myvenues.main.repository

import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.main.facadePattern.FacadePatternRepository
import ir.maghsoodi.myvenues.utils.Constants.Companion.EXPIRE_TIME_RANGE
import ir.maghsoodi.myvenues.utils.Resource
import ir.maghsoodi.myvenues.utils.TimeManagement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
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

    var isSuccessYet = false

    suspend fun getNearVenues(lat: Double, lng: Double) {
        Timber.tag("loadData repository")
            .d("updateVenueList ${facadeRepository.hasNearestMetaInDB(lat, lng)}")
        _venuesFlow.value = SearchEvent.Loading
        getDataFromDb(lat, lng)
        getDataFromNet(lat, lng)
    }

    private suspend fun getDataFromDb(lat: Double, lng: Double) {
        facadeRepository.deleteOldMeta(timeManagement.getCurrentUnixTime() - EXPIRE_TIME_RANGE)
        if (facadeRepository.hasNearestMetaInDB(lat, lng)) {
            val metaEntity = facadeRepository.getNearestMetaInDB(lat, lng)
            val venueEntities = facadeRepository.getVenueEntitiesOfMetaFromDB(metaEntity)
            _venuesFlow.value = SearchEvent.Success(venueEntities)
            isSuccessYet = true
        }
    }

    private suspend fun getDataFromNet(lat: Double, lng: Double) {
        when (val searchResponse = facadeRepository.searchNearVenueFromRemote(lat, lng)) {
            is Resource.Error -> {
                if (!isSuccessYet)
                    _venuesFlow.value = SearchEvent.Failure(searchResponse.message!!)
            }
            is Resource.Success -> {
                _venuesFlow.value = SearchEvent.Success(
                    searchResponse.data!!.response!!.venues
                )
                saveToDB(searchResponse, lat, lng)
            }
        }
    }

    suspend fun saveToDB(serachResponse: Resource<SearchResponse>, lat: Double, lng: Double) {
        facadeRepository.run {
            saveMetaEntityIntoDB(serachResponse.data!!.meta, lat, lng)
            saveVenueEntityIntoDB(
                metaEntity = serachResponse.data.meta,
                venueEntities = serachResponse.data.response.venues
            )
        }
    }
}