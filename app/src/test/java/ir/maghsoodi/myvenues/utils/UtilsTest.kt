package ir.maghsoodi.myvenues.utils

import com.google.common.collect.Range
import com.google.common.truth.Truth
import org.junit.Assert.*
import org.junit.Test

class UtilsTest {

    @Test
    fun testCalculateDistance() {
        Truth.assertThat(
            Utils.calculateDistance(
                35.7548931,
                51.4934737,
                35.75870200242442,
                51.48489475250244
            )
        ).isEqualTo(883)

        Truth.assertThat(
            Utils.calculateDistance(
                35.7548931,
                51.4934737,
                35.755949749026726,
                51.481611552593606
            )
        ).isEqualTo(1078)
    }
}