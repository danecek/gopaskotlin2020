package osoby

import java.util.*

class Osoba(val name: String, var rodic: Osoba? = null, val deti : MutableMap<String, Osoba> = mutableMapOf()) {


   fun addPotomek(potomek: Osoba) {
       potomek.rodic = this
       deti[potomek.name] = potomek
   }

   fun najdiVnukaC(diteS: String, vnukS: String) : Osoba? {

   }
    fun najdiVnukaO(diteS: String, vnukS: String) : Optional<Osoba> {

    }
    fun najdiVnukaK(diteS: String, vnukS: String) : Osoba? {

    }
}

fun main(args: Array<String>) {
    println("Hello World!")
    val ded = Osoba("ded")
    val otec = Osoba("otec")
    val vnuk = Osoba("vnuk")
    ded.addPotomek(otec)
    otec.addPotomek(vnuk)
}