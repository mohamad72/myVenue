package ir.maghsoodi.myvenues.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetaEntity(
    @PrimaryKey(autoGenerate = false)
    val requestId: String,
    val code: Int,
    var lat: Double,
    var lng: Double,
    var created_at: Long,
    var errorDetail: String=""
)