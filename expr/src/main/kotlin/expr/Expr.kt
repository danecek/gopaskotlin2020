package expr

import expr.Oper.*
import java.lang.UnsupportedOperationException

interface ExprPatt {
    fun match(e: Expr): Expr?
}

open class UnOpPat(val op: Oper) : ExprPatt {
    override fun match(e: Expr): UnOp? {
        return if (e is UnOp && e.op == op) e else null
    }
}

object UnPlusPat : UnOpPat(PLUS)

object UnMnsMns : UnOpPat(MINUS)

object UnMnsMnsPat : ExprPatt {
    override fun match(e: Expr): UnOp? {
        return if (e !is UnOp || e.op != MINUS) null
        else {
            val eoprnd = e.oprnd;
            if (eoprnd !is UnOpPat || eoprnd.op != MINUS) null
            else e
        }
    }
}

interface Expr {
    val value: Int
}

data class Num(override val value: Int) : Expr {
    override fun toString() = value.toString()
}

data class UnOp(val op: Oper, val oprnd: Expr) : Expr {
    override val value = when (op) {
        MINUS -> -oprnd.value
        PLUS -> oprnd.value
        else -> throw UnsupportedOperationException()
    }

    override fun toString() = "$op$oprnd"
}

fun Expr.optim(): Expr {
    return UnPlusPat.match(this)?.oprnd?.optim() ?:
    ((UnMnsMnsPat.match(this)?.oprnd as? UnOp))?.oprnd?.optim() ?:
    this
}

fun main() {
    val unmns = UnOp(MINUS, UnOp(MINUS, Num(3)))
    println(unmns.value) // -3
    println(unmns.toString())  // -3
    println(unmns.optim())  // -3
    println(unmns.optim().value)  // -3
    val unpls = unmns.copy(op = PLUS)
    println(unpls)
    println(unpls.optim())
    println(unmns == UnOp(MINUS, Num(3))) // true
    println(unmns === UnOp(MINUS, Num(3))) // false
}