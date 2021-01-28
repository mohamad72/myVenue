package ir.maghsoodi.myvenues

import ir.maghsoodi.myvenues.data.models.MetaEntity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val m1 = MetaEntity("something1", 200, 98.765, 12.345, System.currentTimeMillis(), "")
        val m2 = MetaEntity("something2", 200, 98.765, 12.345, System.currentTimeMillis(), "")
        val m3 = MetaEntity("something3", 200, 98.765, 12.345, System.currentTimeMillis(), "")
        val m4 = MetaEntity("something4", 200, 98.765, 12.345, System.currentTimeMillis(), "")
        val m5 = MetaEntity("something5", 200, 98.765, 12.345, System.currentTimeMillis(), "")

        var clike = emptyList<MetaEntity>()
        val javalike = listOf(m1,m2)
//        val jadvalike = listOf(m1,m5)
//        clike = clike.minus(javalike)
        clike = clike.plus(javalike)
        clike = clike.minus(m1)
//        clike = clike.minus(jadvalike)
//        clike = clike.plus(jadvalike)
        println(clike)
    }
}