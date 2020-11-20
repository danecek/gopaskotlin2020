package osoby

import java.util.*

class Osoba(val name: String, var rodic: Osoba? = null, val deti : MutableMap<String, Osoba> = mutableMapOf()) {

   fun addPotomek(potomek: Osoba) {
       potomek.rodic = this
       deti[potomek.name] = potomek
   }

   fun najdiVnukaClassik(diteS: String, vnukS: String) : Osoba? {

   }
    fun najdiVnukaFunctional(diteS: String, vnukS: String) : Optional<Osoba> {

    }
    fun najdiVnukaKotlin(diteS: String, vnukS: String) : Osoba? {

    }
}

fun Osoba.rodic(name: String, potomci: Osoba.(name: String) -> Unit) {

}

fun main(args: Array<String>) {
    println("Hello World!")
    val ded = Osoba("ded")
    val otec = Osoba("otec")
    val vnuk = Osoba("vnuk")
    ded.addPotomek(otec)
    otec.addPotomek(vnuk)
    val patr = patriarcha("praded") {
        this.rodic("ded1") {
            this.rodic("otec") {
                this. potomek("vnuk1")
                this.potomek("vnuk2")
            }
            this.rodic("matka") {

            }
        }
        this.rodic("ded2") {
            this.potomek("otec2")
        }
    }
    println(ded.najdiVnukaClassik("otec", "vnuk"))
}