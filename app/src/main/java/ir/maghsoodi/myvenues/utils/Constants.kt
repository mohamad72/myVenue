package ir.maghsoodi.myvenues.utils

class Constants {
    companion object {
        const val BASE_URL = "https://api.foursquare.com/v2/venues/"
        const val LIMIT_FOR_EVERY_REQUEST = 50
        const val RADIUS_FOR_SEARCH = 200
        const val MAXIMUM_NEAR_DISTANCE = 100
        const val NUMBER_OF_ITEMS_IN_EACH_PAGE = 15
        const val INTERNET_IS_ONLINE_MESSAGE = "INTERNET_IS_ONLINE_MESSAGE"
        const val VERSION_API = 20120609
        const val FAKE_CURRENT_TIME = 1611666165000
        const val MINIMUM_EXPIRE_TIME = 1511666165000
        const val EXPIRE_TIME_RANGE: Long = 48 * 24 * 60 * 60 * 1000L

        const val REQUEST_CODE_LOCATION_PERMISSION = 1

        const val DATABASE_NAME = "venue_db"
    }
}