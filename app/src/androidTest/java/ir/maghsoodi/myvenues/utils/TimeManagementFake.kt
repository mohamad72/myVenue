package ir.maghsoodi.myvenues.utils

import ir.maghsoodi.myvenues.utils.Constants.Companion.FAKE_CURRENT_TIME

class TimeManagementFake : TimeManagement {
    override fun getCurrentUnixTime(): Long {
        return FAKE_CURRENT_TIME
    }
}