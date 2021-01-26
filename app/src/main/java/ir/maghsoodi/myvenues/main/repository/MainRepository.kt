package ir.maghsoodi.myvenues.main.repository

import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Resource


interface MainRepository {

    suspend fun getNearVenues(lat: Double, lng: Double)

}