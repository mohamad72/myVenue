package ir.maghsoodi.myvenues.data.models

import androidx.room.paging.LimitOffsetDataSource
import ir.maghsoodi.myvenues.BuildConfig
import ir.maghsoodi.myvenues.utils.Constants
import ir.maghsoodi.myvenues.utils.Constants.Companion.LIMIT_FOR_EVERY_REQUEST
import ir.maghsoodi.myvenues.utils.Constants.Companion.RADIUS_FOR_SEARCH
import ir.maghsoodi.myvenues.utils.Constants.Companion.VERSION_API
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFoursquare {
    @GET("search/")
    suspend fun getVenuesOfThisLocation(
        @Query("radius") radius: Int = RADIUS_FOR_SEARCH,
        @Query("limit") limit: Int = LIMIT_FOR_EVERY_REQUEST,
        @Query("ll") location: String,
        @Query("v") versionApi: Int = VERSION_API,
        @Query("client_secret") client_secret: String = BuildConfig.client_secret,
        @Query("client_id") client_id: String = BuildConfig.client_id
    ): Response<SearchResponse>
}