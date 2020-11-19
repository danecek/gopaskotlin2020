package expr

import expr.Oper.*

interface Expr {
    val value: Int
}

data class Num(override val value: Int) : Expr {
    override fun toString()= value.toString()
}

data class UnOp(val op: Oper, val oprnd: Expr) : Expr {
    override val value = when (op) {
        MINUS -> -oprnd.value
        PLUS -> oprnd.value
    }
    override fun toString() = "$op$oprnd"
}

fun main() {
    val unmns = UnOp(MINUS, Num(3))
    println(unmns.value) // -3
    println(unmns.toString())  // -3
    val unpls = unmns.copy(op = PLUS)
    println(unpls)
    println(unmns == UnOp(MINUS, Num(3)))
}