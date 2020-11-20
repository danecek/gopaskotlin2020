package osoby

import java.util.*

class Osoba(val name: String, var rodic: Osoba? = null, val deti: MutableMap<String, Osoba> = mutableMapOf()) {

    fun addPotomek(potomek: Osoba) {
        potomek.rodic = this
        deti[potomek.name] = potomek
    }

    fun najdiDite(diteS: String): Optional<Osoba> {
        return Optional.ofNullable(deti[diteS])
    }

    fun najdiVnukaFunctional(diteS: String, vnukS: String): Optional<Osoba> {
        return Optional.ofNullable(deti[diteS]).flatMap { Optional.ofNullable(it.deti[vnukS]) }
    }

    fun najdiVnukaKotlin(diteS: String, vnukS: String): Osoba? {
        return null;

    }

    override fun toString(): String {
        return "Osoba(name='$name', deti=$deti)"
    }

}

fun Osoba.potomek(name: String, potomci: (Osoba.() -> Unit)? = null) {

}

fun main(args: Array<String>) {
    val praded = Osoba("praded").apply {
        potomek("ded1") {
            potomek("otec") {
                potomek("vnuk1")
                potomek("vnuk2")
            }
            potomek("matka") {

            }
        }
        potomek("ded2") {
            potomek("otec2")
        }
    }

    println(praded.najdiVnukaFunctional("ded1", "otec").get())
}