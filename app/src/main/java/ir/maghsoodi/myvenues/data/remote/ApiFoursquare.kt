package ir.maghsoodi.myvenues.data.models

import ir.maghsoodi.myvenues.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFoursquare {
    @GET("search/")
    suspend fun getVenuesOfThisLocation(
        @Query("radius") radius: Int = 200,
        @Query("ll") location: String,
        @Query("v") versionApi: Int = 20120609,
        @Query("client_secret") client_secret: String = BuildConfig.client_secret,
        @Query("client_id") client_id: String = BuildConfig.client_id
    ): Response<SearchResponse>
}