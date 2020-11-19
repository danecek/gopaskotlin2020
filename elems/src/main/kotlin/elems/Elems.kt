package elems

class Elem {
    override fun toString(): String = content.joinToString(separator = "\n")

    val content: Array<String>

    val w : Int
            get() = content[0].length
    val h : Int
            get() = content.size

    constructor(ch: String, w: Int, h: Int) {
        content = Array(h) { (ch.repeat(w)) }
    }

    constructor(w: Int, h: Int) : this(" ", w, h)

    constructor(_content: Array<String>) {
        content = _content
    }

    infix fun above(that: Elem): Elem {
        assert(that.w == w)
        return Elem(this.content + that.content)
        //Array(this.h() + that.h()) {   }
    }

    infix fun beside(that: Elem): Elem {
        assert(that.w == w)
        return Elem(Array(h){ i -> this.content[i] + that.content[i]  })
    }

    infix fun widenR(newW: Int): Elem {
        assert(w <= newW)
        return this beside Elem(newW- w, h)

    }
    infix fun widenL(newW: Int): Elem {
        assert(w <= newW)
        return Elem(newW- w, h)  beside this
    }
}

fun main() {
    val e1 = Elem(arrayOf("abc", "xyz"))
    val e2 = Elem("*", 6, 4)
    println( e1.widenL(e2.w) above e2)

}