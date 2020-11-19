package elems

import java.util.*

abstract class Elem2 {

    protected fun myToString(): String = content.joinToString(separator = "\n")

    open abstract val content: Array<String>

    open val w: Int
        get() = content[0].length
    open val h: Int
        get() = content.size

    infix fun above(that: Elem2): Elem2 {
        assert(that.w == w)
        return BasicElem(this.content + that.content)
    }

    infix fun beside(that: Elem2): Elem2 {
        assert(that.w == w)
        return BasicElem(Array(h) { i -> this.content[i] + that.content[i] })
    }


    infix fun widenR(newW: Int): Elem2 {
        assert(w <= newW)
        return this beside empty(newW - w, h)

    }

    infix fun widenL(newW: Int): Elem2 {
        assert(w <= newW)
        return empty(newW - w, h) beside this
    }

    companion object {
        fun char(ch: Char, w: Int, h: Int) = BasicElem(Array(h) { ch.toString().repeat(w) })
        fun empty(w: Int, h: Int) = char(' ', w, h)
        val magicSquare = BasicElem(arrayOf("123", "456", "789"))
    }
}

data class BasicElem(override val content: Array<String>) : Elem2() {
    override fun toString(): String = myToString()

    override fun equals(other: Any?): Boolean {
        return if (other !is BasicElem) false
        else Arrays.equals(content, other.content)
    }

    object MagicSquare2 : Elem2() {
        override val content: Array<String> = arrayOf("123", "456", "789")
    }
}

object MagicSquare3 : Elem2() {
    override val content: Array<String> = arrayOf("123", "456", "789")
}

fun triangle(n: Int): Elem2 {
    return if (n == 1) Elem2.char('*', 1, 1) else {
        val subelem = triangle(n - 1)
        subelem above Elem2.char('*', n, 1)
    }
}

fun main() {
    val t4 = triangle(4)
    println(t4 == triangle(4))
    println(t4)

}