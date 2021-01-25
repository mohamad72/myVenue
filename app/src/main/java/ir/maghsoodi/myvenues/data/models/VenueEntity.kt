package ir.maghsoodi.myvenues.data.models

import ir.maghsoodi.myvenues.data.models.*

data class VenueEntity(
    val categories: List<Category>,
    val contact: Contact,
    val hasPerk: Boolean,
    val id: String,
    val location: Location,
    val name: String,
    val referralId: String,
    val venueChains: List<Any>
)