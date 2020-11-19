package expr

enum class Oper(val repr: String, val pri: Int) {
    PLUS("+", 2),
    MINUS("-", 2),
    TIMES("*", 1),
    DIV("/", 1)
    ;

    override fun toString(): String = repr

}