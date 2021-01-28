package ir.maghsoodi.myvenues.data.models

data class Location(
    val address: String?,
    val city: String?,
    val country: String,
    val crossStreet: String,
    val distance: Int,
    val lat: Double,
    val lng: Double,
    val state: String?
){
    fun getShortAddress():String
    {
        address?.let { return address }
        city?.let { return city }
        state?.let { return state }
        return country
    }

    fun getFullAddress():String
    {
        address?.let { return "${this.country}, ${this.state}, ${this.city}, ${this.address}" }
        city?.let { return "${this.country}, ${this.state}, ${this.city}" }
        state?.let { return "${this.country}, ${this.state}" }
        return country
    }
}