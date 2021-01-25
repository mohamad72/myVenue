package ir.maghsoodi.myvenues.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meta_table")
data class MetaEntity(
    @PrimaryKey(autoGenerate = false)
    val requestId: String,
    val code: Int
)