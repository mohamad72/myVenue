package ir.maghsoodi.myvenues.Repository

import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Resource
import javax.inject.Inject

class FacadePatternRepository @Inject constructor(
    val dbController: DBController,
    val remoteController: RemoteController
) {

    suspend fun searchNearVenueFromRemote(lat: Double, lng: Double): Resource<SearchResponse> {
        return remoteController.searchNearVenueFromRemote(lat, lng)
    }

    suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity) {
        return dbController.saveMetaEntityIntoDB(metaEntity)
    }

    suspend fun saveVenueEntityIntoDB(requestId: String, venueEntities: List<VenueEntity>) {
        return dbController.saveVenueEntityIntoDB(requestId,venueEntities)
    }

    suspend fun hasNearestMeta(lat: Double, lng: Double): Boolean {
        return dbController. hasNearestMeta(lat,lng)
    }

    suspend fun getNearestMeta(lat: Double, lng: Double): MetaEntity {
        return dbController.getNearestMeta(lat,lng)
    }

    suspend fun getVenueEntitiesOfMeta(metaEntity: MetaEntity): List<VenueEntity> {
        return dbController.getVenueEntitiesOfMeta(metaEntity)
    }


    suspend fun deleteOldMeta(expireUnixTime: Long) {
        return dbController.deleteOldMeta(expireUnixTime)
    }
}