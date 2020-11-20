import java.util.*

class Osoba(
    val name: String, var rodic: Osoba? = null,
    val deti: MutableMap<String, Osoba> = mutableMapOf()
) {
    //*************************************** Klasicky
    fun diteC(name: String): Osoba? {
        return deti.get(name)
    }

    fun vnukC(dite: String, vnuk: String): Osoba? {
        val dit: Osoba? = diteC(dite)
        return if (dit == null) null
        else dit.deti[vnuk]
    }

    fun praVnukC(dite: String, vnuk: String, pravnuk: String): Osoba? {
        val vnk: Osoba? = vnukC(dite, vnuk)
        return if (vnk == null) null
        else vnk.deti[pravnuk]
    }
//*************************************** Monada
    fun diteO(name: String): Optional<Osoba> {
        return Optional.ofNullable(deti.get(name))
    }

    fun vnukO(dite: String, vnuk: String): Optional<Osoba> {
        return diteO(dite).flatMap { diteO(vnuk) }
    }

    fun praVnukO(dite: String, vnuk: String, pravnuk: String): Optional<Osoba> {
        return vnukO(dite, pravnuk).flatMap { diteO(pravnuk) }
    }

    //*************************************** Either
    open class Either<out T, out U> {
       class Left<out T, out U>(val error: T) : Either<T, U>()
       class Right<out T, out U>(val value: U) : Either<T, U>()
    }
    val l : Either<Any, Any> = Either.Left("chyha")

    fun x (s: String): Either<Exception, Int> {
        return try {
            Either.Right(s.toInt())
        } catch (e:java.lang.Exception) {  return Either.Left(e) }
    }

    fun diteE(name: String): Optional<Osoba> {
        return Optional.ofNullable(deti.get(name))
    }

    fun vnukE(dite: String, vnuk: String): Optional<Osoba> {
        return diteO(dite).flatMap { diteO(vnuk) }
    }

    fun praVnukE(dite: String, vnuk: String, pravnuk: String): Optional<Osoba> {
        return vnukO(dite, pravnuk).flatMap { diteO(pravnuk) }
    }


    //*************************************** Kotlin
    fun dite(dite: String): Osoba? = deti[dite]

    fun vnuk(dite: String, vnuk: String): Osoba? = deti[dite]?.deti?.get(vnuk)

    fun pravnuk(dite: String, vnuk: String, pravnuk: String): Osoba? =
        deti[dite]?.deti?.get(vnuk)?.deti?.get(pravnuk)


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