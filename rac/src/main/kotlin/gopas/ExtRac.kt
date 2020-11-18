package gopas

fun AbstrRat.neg() = Rat(-this.n, this.d)


fun ratNeg(rat: AbstrRat) = Rat(-rat.n, rat.d)

infix  fun Int.mlt(rat: AbstrRat) = rat mlt this

