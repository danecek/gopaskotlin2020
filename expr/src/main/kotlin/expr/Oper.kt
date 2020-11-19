package expr

enum class Oper {
    MINUS {
        override fun toString(): String = "-"
    },
    PLUS {
        override fun toString(): String = "+"
    }
}

enum class OperVariant(val repr: String) {
    PLUS("-"),
    MINUS("-");

    override fun toString(): String = repr

}