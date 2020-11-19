data class Osoba(
    val name: String, var rodic: Osoba? = null,
    val deti: MutableMap<String, Osoba> = mutableMapOf()
) {

    fun deti(): Collection<Osoba> {
        return deti.values
    }

    fun vnuci(): Collection<Osoba> {
        return deti.values.flatMap { it.deti.values }
    }

    fun pravnuci(): Collection<Osoba> {
        return vnuci().flatMap { it.deti.values }
    }

    fun praprodic(): Osoba? = rodic?.rodic
    fun praprarodic(): Osoba? = praprodic()?.rodic

    fun vnuk(dite: String, vnuk: String): Osoba? = deti[dite]?.deti?.get(vnuk)

    fun pravnuk(dite: String, vnuk: String, pravnuk: String): Osoba? =
        deti[dite]?.deti?.get(vnuk)?.deti?.get(pravnuk)

    fun addDite(o: Osoba) {
        deti[o.name] = (o)
        o.rodic = this
    }

    override fun toString(): String {
        val d = if (deti.isEmpty()) "" else ":\n" + deti
        return "$name$d"
    }

    fun print(n: Int) {
        println(" ".repeat(n) + name)
        deti().forEach { it.print(n + name.length) }
    }
}

fun patriarcha(name: String, potomci: (Osoba.() -> Unit)): Osoba {
    val patr = Osoba(name)
     potomci.invoke(patr)
    return patr
}

fun Osoba.rodic(name: String, potomci: (Osoba.() -> Unit)) {
    val dite = Osoba(name)
    this.addDite(dite)
    potomci.invoke(dite)
}

fun Osoba.potomek(name: String) {
    val dite = Osoba(name)
    this.addDite(dite)
}

fun main() {
    val patr = patriarcha("praded") {
        rodic("ded1") {
            rodic("otec") {
                potomek("vnuk1")
                potomek("vnuk2")
            }
            rodic("matka") {

            }
        }
        rodic("ded2") {
            potomek("otec2")
        }
    }
    patr.print(0)
    println(patr)
    println(patr.vnuci())
    println(patr.pravnuci())
    println(patr.vnuk("ded", "otec"))
}