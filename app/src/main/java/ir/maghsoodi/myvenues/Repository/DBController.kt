package ir.maghsoodi.myvenues.Repository

import ir.maghsoodi.myvenues.data.db.VenueDao
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity
import ir.maghsoodi.myvenues.utils.TimeManagement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.net.CacheRequest
import java.util.ArrayList
import javax.inject.Inject


class DBController @Inject constructor(
    val venueDao: VenueDao,
    val timeManagement: TimeManagement
) {

    suspend fun saveMetaEntityIntoDB(metaEntity: MetaEntity) {
        metaEntity.created_at = timeManagement.getCurrentUnixTime()
        venueDao.insertMeta(metaEntity)
    }

    suspend fun saveVenueEntityIntoDB(requestId: String, venueEntities: List<VenueEntity>) {
        venueEntities.forEach {
            it.requestId = requestId
            venueDao.insertVenue(it)
        }
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