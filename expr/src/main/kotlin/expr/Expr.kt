package expr

import expr.Oper.*

interface Expr {
    val value: Int
    val pri: Int
}

data class Num(override val value: Int) : Expr {
    override val pri= 0
    override fun toString() = value.toString()
}

data class BinOp(val op: Oper, val left: Expr, val right: Expr) : Expr {
    override val pri= op.pri
    override val value = when (op) {
        MINUS -> left.value - right.value
        PLUS -> left.value + right.value
        TIMES -> left.value * right.value
        DIV -> left.value / right.value
        else -> throw UnsupportedOperationException()
    }
    val ls = if (pri < left.pri) "($left)" else "$left"
    val rs = if (pri < right.pri) "($right)" else "$right"
    override fun toString() = "$ls $op $rs"
}

data class UnOp(val op: Oper, val oprnd: Expr) : Expr {
    override val pri= 1
    override val value = when (op) {
        MINUS -> -oprnd.value
        PLUS -> oprnd.value
        else -> throw UnsupportedOperationException()
    }
    override fun toString() = "$op$oprnd"
}

fun main() {
    val bop = BinOp(TIMES, BinOp(PLUS, Num(1),  Num(2)),  Num(3))
    println(bop)
    println(bop.value)
    val unmnsmns = UnOp(MINUS, UnOp(MINUS, Num(3)))
    println(unmnsmns)  // --3
    println(unmnsmns.value) // 3
    println(unmnsmns.optim())  // 3
    println(unmnsmns.optim().value)  // 3
    val pluszero = BinOp(PLUS, Num(0), UnOp(PLUS, Num(1)))
    println(pluszero)
    println(pluszero.optim())
}