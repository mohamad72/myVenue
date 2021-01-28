package ir.maghsoodi.myvenues.main

import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Constants.Companion.MAXIMUM_NEAR_DISTANCE
import ir.maghsoodi.myvenues.utils.TimeManagement
import ir.maghsoodi.myvenues.utils.Utils
import javax.inject.Inject

class DBController @Inject constructor(
    val venueDao: VenueDao,
    val timeManagement: TimeManagement
) {

    suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity) {
        metaEntity.created_at = timeManagement.getCurrentUnixTime()
        metaEntity.errorDetail = ""
        venueDao.insertMeta(metaEntity)
    }

    suspend fun saveVenueEntityIntoDB(metaEntity: MetaEntity, venueEntities: List<VenueEntity>) {
        venueEntities.forEach {
            it.requestId = metaEntity.requestId
            venueDao.insertVenue(it)
        }
    }

    suspend fun hasNearestMeta(lat: Double, lng: Double): Boolean {
        var metaEntities = venueDao.getAllMetaCalls()
        metaEntities.forEach {
            if (Utils.calculateDistance(lat, lng, it.lat, it.lng) < MAXIMUM_NEAR_DISTANCE)
                return true
        }
        return false
    }

    suspend fun getNearestMeta(lat: Double, lng: Double): MetaEntity {
        var metaEntities = venueDao.getAllMetaCalls()
        var minDistance: Int = Int.MAX_VALUE
        var nearestMeta: MetaEntity = metaEntities.get(0)
        metaEntities.forEach {
            if (Utils.calculateDistance(lat, lng, it.lat, it.lng) < minDistance) {
                nearestMeta = it
                minDistance = Utils.calculateDistance(lat, lng, it.lat, it.lng)
            }
        }
        return nearestMeta
    }

    suspend fun getVenueEntitiesOfMeta(metaEntity: MetaEntity): List<VenueEntity> {
        venueDao.getMetaWithVenues(metaEntity.requestId).get(0).let {
            return it.venues
        }
        return arrayListOf<VenueEntity>()
    }


    suspend fun deleteOldMeta(expireUnixTime: Long) {
        venueDao.deleteOldMeta(expireUnixTime)
    }
}