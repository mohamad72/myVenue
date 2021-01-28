package ir.maghsoodi.myvenues.main.facadePattern

import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Resource

interface FacadePatternRepository {

    suspend fun searchNearVenueFromRemote(lat: Double, lng: Double): Resource<SearchResponse>

    suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity, lat: Double, lng: Double)

    suspend fun saveVenueEntityIntoDB(metaEntity: MetaEntity, venueEntities: List<VenueEntity>)

    suspend fun hasNearestMetaInDB(lat: Double, lng: Double): Boolean

    suspend fun getNearestMetaInDB(lat: Double, lng: Double): MetaEntity

    suspend fun getVenueEntitiesOfMetaFromDB(metaEntity: MetaEntity): List<VenueEntity>

    suspend fun deleteOldMeta(expireUnixTime: Long)

}