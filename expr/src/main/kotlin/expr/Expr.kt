package expr

import expr.Oper.*
import java.lang.UnsupportedOperationException

interface ExprPatt {
   fun match(e: Expr) : Expr?
}

object UnivPatt : ExprPatt {
    override fun match(e: Expr): Expr? = e
}
object UnPat: ExprPatt{
    override fun match(e: Expr): UnOp? = e as? UnOp
}

object UnPlusPat: ExprPatt{
    override fun match(e: Expr): UnOp? {
       return if (e is UnOp && e.op == PLUS) e else null
    }
}

class UnOpPat(val op: Oper): ExprPatt{

    override fun match(e: Expr): UnOp? {
        return if (e is UnOp && e.op == op) e else null
    }
}

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
        else -> throw UnsupportedOperationException()
    }
    override fun toString() = "$op$oprnd"
}

fun Expr.optim() : Expr {
    return UnOpPat(PLUS).match(this)?.oprnd?.optim() ?:
    this
}

fun main() {
    val unmns = UnOp(MINUS, Num(3))
    println(unmns.value) // -3
    println(unmns.toString())  // -3
    val unpls = unmns.copy(op = PLUS)
    println(unpls)
    println(unpls.optim())
    println(unmns == UnOp(MINUS, Num(3))) // true
    println(unmns === UnOp(MINUS, Num(3))) // false
}