package ir.maghsoodi.myvenues.data.models

data class Location(
    val address: String,
    val city: String,
    val country: String,
    val crossStreet: String,
    val distance: Int,
    val lat: Double,
    val lng: Double,
    val state: String
)