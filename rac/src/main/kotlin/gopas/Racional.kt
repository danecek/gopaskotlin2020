package gopas

sealed class AbstrRat(val n: Int, val d: Int= 1) {

    override fun toString(): String = if (d!=1)"$n/$d" else "$n"


    infix fun mlt(that: AbstrRat): Rat = Rat(this.n * that.n, this.d * that.d)
    infix fun mlt(that: Int): Rat = this mlt Rat(that)
    fun inv() = Rat(d, n)

    infix fun div(that: Rat) = this.inv() mlt that

    //    fun equals(other: Any?): Boolean  = this === other


    override fun equals(other: Any?): Boolean {
        return if (other == null) false
        else if (other !is Rat) false
        else {
            val thisn = norm()
            val othern = other.norm()
            thisn.n == othern.n && thisn.d == othern.d
        }
    }

    override fun hashCode(): Int = n xor d;

    fun norm(): Rat {
        fun gcd(n: Int, d: Int): Int =
            if (d == 0) n
            else gcd(d, n % d)

        val gcd = gcd(n, d)
        return Rat(n / gcd, d / gcd)
    }

}
class Rat(n: Int, d: Int = 1) : AbstrRat(n, d)

object ONE : AbstrRat(1)
object ZERO : AbstrRat(0)

fun main() {
    println(Rat(3) mlt Rat(3, 5))
    println(Rat(3) div Rat(3, 5))
    println(Rat(3, 5) === Rat(3, 5))
    println(Rat(3, 5) == Rat(3, 5)) // println(Rat(3, 5).equals(Rat(3,5)))

    println(Rat(6, 10).norm())

    println(Rat(6, 10) == Rat(3, 5))
    println((Rat(3,5)  mlt 5).norm())
    println(ONE mlt ZERO)


    println(ratNeg(ratNeg(ONE)))
    println(ONE.neg().neg())

    println(3 mlt ONE)
}

