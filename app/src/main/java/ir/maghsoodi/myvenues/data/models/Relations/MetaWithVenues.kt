package ir.maghsoodi.myvenues.data.models.Relations

import androidx.room.Embedded
import androidx.room.Relation
import ir.maghsoodi.myvenues.data.models.MetaEntity
import ir.maghsoodi.myvenues.data.models.VenueEntity

data class MetaWithVenues(
    @Embedded val meta: MetaEntity,
    @Relation(
        parentColumn = "requestId",
        entityColumn = "requestId"
    )
    val venues: List<VenueEntity>
)