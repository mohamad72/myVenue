package ir.maghsoodi.myvenues.main

import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.Constants.Companion.MAXIMUM_NEAR_DISTANCE
import ir.maghsoodi.myvenues.utils.TimeManagement
import ir.maghsoodi.myvenues.utils.Utils
import kotlinx.coroutines.flow.merge
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ln

class DBController @Inject constructor(
    val venueDao: VenueDao,
    val timeManagement: TimeManagement
) {

    suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity, lat: Double, lng: Double) {
        metaEntity.created_at = timeManagement.getCurrentUnixTime()
        metaEntity.errorDetail = ""
        metaEntity.lat = lat
        metaEntity.lng = lng
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
            Timber.tag("location")
                .d("distance is= ${Utils.calculateDistance(lat, lng, it.lat, it.lng)}")
            if (Utils.calculateDistance(lat, lng, it.lat, it.lng) < MAXIMUM_NEAR_DISTANCE)
                return true
        }
        return false
    }

    suspend fun getNearestMeta(lat: Double, lng: Double): List<MetaEntity> {
        val metaEntities = venueDao.getAllMetaCalls()
        metaEntities.forEach {
            if (Utils.calculateDistance(lat, lng, it.lat, it.lng) > MAXIMUM_NEAR_DISTANCE) {
                metaEntities.minus(it)
            }
        }
        return metaEntities
    }

    suspend fun getVenueEntitiesOfMeta(metaEntities: List<MetaEntity>): List<VenueEntity> {
        Timber.tag("near_point").d(metaEntities.size.toString())
        var venueEntities: MutableList<VenueEntity> = emptyList<VenueEntity>().toMutableList()
        metaEntities.forEach { metaEntity ->
            venueDao.getMetaWithVenues(metaEntity.requestId)[0].let {
                venueEntities.addAll(it.venues)
            }
        }
        Timber.tag("near_point").d(venueEntities.size.toString())
        return venueEntities
    }


    suspend fun deleteOldMeta(expireUnixTime: Long) {
        venueDao.deleteOldMeta(expireUnixTime)
    }
}