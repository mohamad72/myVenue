package ir.maghsoodi.myvenues.main.facadePattern

import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.SearchResponse
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.main.DBController
import ir.maghsoodi.myvenues.main.RemoteController
import ir.maghsoodi.myvenues.utils.Resource
import javax.inject.Inject

class FacadePatternRepositoryDefault @Inject constructor(
    val dbController: DBController,
    val remoteController: RemoteController
) : FacadePatternRepository {

    override suspend fun searchNearVenueFromRemote(lat: Double, lng: Double): Resource<SearchResponse> {
        return remoteController.searchNearVenueFromRemote(lat, lng)
    }

    override suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity, lat: Double, lng: Double) {
        return dbController.saveMetaEntityIntoDB(metaEntity, lat, lng)
    }

    override suspend fun saveVenueEntityIntoDB(metaEntity: MetaEntity, venueEntities: List<VenueEntity>) {
        return dbController.saveVenueEntityIntoDB(metaEntity, venueEntities)
    }

    override suspend fun hasNearestMetaInDB(lat: Double, lng: Double): Boolean {
        return dbController.hasNearestMeta(lat, lng)
    }

    override suspend fun getNearestMetaInDB(lat: Double, lng: Double): List<MetaEntity> {
        return dbController.getNearestMeta(lat, lng)
    }

    override suspend fun getVenueEntitiesOfMetaFromDB(metaEntities: List<MetaEntity>): List<VenueEntity> {
        return dbController.getVenueEntitiesOfMeta(metaEntities)
    }


    override suspend fun deleteOldMeta(expireUnixTime: Long) {
        return dbController.deleteOldMeta(expireUnixTime)
    }
}