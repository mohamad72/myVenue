package ir.maghsoodi.myvenues.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity

@Database(
    entities = [
        VenueEntity::class,
        MetaEntity::class
    ],
    version = 1
)
abstract class RunningDatabase : RoomDatabase() {

    abstract fun getVenueDao(): VenueDao
}