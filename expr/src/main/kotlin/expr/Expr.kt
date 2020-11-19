package expr

interface Expr {
   val value: Int
}

data class Num(override val value: Int) : Expr


fun main() {
    println(Num(3).value)
    val e1 = UnOp(Oper.MINUS, Num(3))
    println(e1.value) // -3
    println(e1.toString)  // -3
}