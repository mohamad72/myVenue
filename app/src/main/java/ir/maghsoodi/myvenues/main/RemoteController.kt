package ir.maghsoodi.myvenues.main

import ir.maghsoodi.myvenues.data.models.ApiFoursquare
import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.utils.Resource
import javax.inject.Inject

class RemoteController @Inject constructor(
    val apiFoursquare: ApiFoursquare
) {

    suspend fun searchNearVenueFromRemote(lat: Double, lng: Double): Resource<SearchResponse> {
        return try {
            val response = apiFoursquare.getVenuesOfThisLocation(location = "$lat,$lng")
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else if (result != null) {
                Resource.Error(result.meta.errorDetail)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured")
        }
    }
}