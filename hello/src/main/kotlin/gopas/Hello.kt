package gopas

open class Cust(_name: String="Fraser") {
    val name = _name

    override fun toString()= "Cust(name='$name')"
    
}

object JimCust : Cust("Jim") {
    override fun toString() = name
}

val johnCust = Cust("John")


fun main() {
    println("Hello, ${Cust()}")
    println("Hello, ${Cust("Tom")}")
    println("Hello, ${johnCust}")
    println("Hello, $JimCust")
}

