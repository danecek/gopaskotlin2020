package expr

interface ExprPatt {
    fun match(e: Expr?): Expr?
}

object UnivPatt : ExprPatt {  // matchuje vzdy
    override fun match(e: Expr?): Expr? = e
}

open class NumPatt(val value: Int) : ExprPatt {
    override fun match(e: Expr?): Expr? = (e as? Num)?.let { if (it.value == value) e else null }
}

object ZeroPatt : NumPatt(0)

object OnePatt : NumPatt(0)

open class UnOpPat(
    val opers: Set<Oper> = setOf(* Oper.values()),
    val orndPatt: ExprPatt = UnivPatt
) : ExprPatt {
    override fun match(e: Expr?): UnOp? {
        return (e as? UnOp)?.let { if (it.op in opers) it else null }?.let { orndPatt.match(it) as UnOp }
    }
}

object UnPlusPatt : UnOpPat(setOf(Oper.PLUS))

object UnMnsPatt : UnOpPat(setOf(Oper.MINUS))

object UnMnsMnsPat : UnOpPat(setOf(Oper.MINUS), UnMnsPatt)

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

fun Expr.optim(): Expr {
    return let { UnPlusPatt.match(it) }?.oprnd?.optim()
        ?: let { UnMnsMnsPat.match(it) }?.let { it.oprnd as? UnOp }?.oprnd?.optim()
        ?: let { UnOpPat().match(it) }?.apply { copy(oprnd = oprnd.optim()) }
        ?: let { PlusZeroPat.match(it) }?.left?.optim()
        ?: let { ZeroPlusPat.match(it) }?.right?.optim()
        ?: let { BinOpPat().match(it) }?.apply { copy(left = left.optim(), right = right.optim()) }
        ?: let { UnivPatt.match(it) }
        ?: this
}
