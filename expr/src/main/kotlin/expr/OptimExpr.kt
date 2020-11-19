package expr

interface ExprPatt {
    fun match(e: Expr?): Expr?
}

fun Expr?.checkPatt(patt: ExprPatt?): Boolean = patt == null || patt.match(this) != null
fun <T : Expr?> T.checkCond(predicate: (T) -> Boolean): T? = if (predicate(this)) this else null

object UnivPatt : ExprPatt {  // matchuje vzdy
    override fun match(e: Expr?): Expr? = e
}

open class UnOpPat(val op: Oper? = null) : ExprPatt {
    override fun match(e: Expr?): UnOp? {
        return (e as? UnOp)?.checkCond { op != null && it.op == op }
    }
}

object UnPlusPatt : UnOpPat(Oper.PLUS)

object UnMnsPatt : UnOpPat(Oper.MINUS)

open class NumPatt(val value: Int) : ExprPatt {
    override fun match(e: Expr?): Expr? = if ((e as? Num)?.value == value) e else null
}

object ZeroPatt : NumPatt(0)

object OnePatt : NumPatt(0)

open class BinOpPat(
    val op: Oper? = null, val leftPatt: ExprPatt = UnivPatt,
    val rightPatt: ExprPatt = UnivPatt
) : ExprPatt {
    override fun match(e: Expr?): BinOp? =
        if (e is BinOp &&
            e.op == op &&
            leftPatt.match(e.left) != null &&
            rightPatt.match(e.right) != null
        ) e
        else null
}

object PlusZeroPat : BinOpPat(Oper.PLUS, rightPatt = ZeroPatt)
object ZeroPlusPat : BinOpPat(Oper.PLUS, leftPatt = ZeroPatt)

object UnMnsMnsPat : ExprPatt {
    override fun match(e: Expr?): UnOp? = UnMnsPatt.match(UnMnsPatt.match(e))
}

fun Expr.optim(): Expr {
    return let { UnPlusPatt.match(it) }?.oprnd?.optim()
        ?: let { UnMnsMnsPat.match(it) }?.let { it.oprnd as? UnOp }?.oprnd?.optim()
        ?: let { UnOpPat().match(it)}?.apply { copy(oprnd = oprnd.optim()) }
        ?: let { PlusZeroPat.match(it) }?.left?.optim()
        ?: let { ZeroPlusPat.match(it) }?.right?.optim()
        ?: let { BinOpPat().match(it) }?.apply { copy(left = left.optim(), right = right.optim()) }
        ?: this
}
