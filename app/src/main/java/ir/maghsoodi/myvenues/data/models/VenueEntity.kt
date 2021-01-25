package ir.maghsoodi.myvenues.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "venue_table")
data class VenueEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val requestId: String,
    val categories: List<Category>,
    val contact: Contact,
    val hasPerk: Boolean,
    val location: Location,
    val name: String,
    val referralId: String
)