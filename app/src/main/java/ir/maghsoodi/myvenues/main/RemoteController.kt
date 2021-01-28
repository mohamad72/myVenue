package ir.maghsoodi.myvenues.main

import ir.maghsoodi.myvenues.data.models.ApiFoursquare
import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.utils.Resource
import timber.log.Timber
import javax.inject.Inject

class RemoteController @Inject constructor(
    val apiFoursquare: ApiFoursquare
) {

    suspend fun searchNearVenueFromRemote(lat: Double, lng: Double): Resource<SearchResponse> {
        return try {
            val response = apiFoursquare.getVenuesOfThisLocation(location = "$lat,$lng")
            val result = response.body()
            Timber.tag("loadData").d("RemoteController ${response.code()}")
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else if (result != null) {
                Timber.tag("loadData").d("error RemoteController ${result.meta.errorDetail}")
                Resource.Error(result.meta.errorDetail)
            } else {
                Timber.tag("loadData").d("null error RemoteController ${response.errorBody()?.string()}")
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
}