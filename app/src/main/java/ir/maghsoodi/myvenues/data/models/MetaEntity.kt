package ir.maghsoodi.myvenues.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetaEntity(
    @PrimaryKey(autoGenerate = false)
    val requestId: String,
    val code: Int,
    val lat: Double,
    val lng: Double,
    val created_at:Long
)