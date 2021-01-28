package ir.maghsoodi.myvenues.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VenueEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var requestId: String,
    var categories: List<Category>,
    val contact: Contact,
    val location: Location,
    val name: String
)