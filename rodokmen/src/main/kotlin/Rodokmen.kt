import java.util.*

sealed class Either<T, U> {
    class Left<T, U>(val error: T) : Either<T, U>() {
        override fun toString(): String {
            return "Left($error)"
        }
    }

    class Right<T, U>(val value: U) : Either<T, U>() {
        override fun toString(): String {
            return "Right($value)"
        }
    }

    fun <R> flatMap(block: U.() -> Either<T, R>): Either<T, R> {
        return when (this) {
            is Left -> Left(error)
            is Right -> {
                block(this.value)
            }
        }
    }
}


class Osoba(
    val name: String, var rodic: Osoba? = null,
    val deti: MutableMap<String, Osoba> = mutableMapOf()
) {
    //*************************************** Klasicky
    fun diteC(name: String): Osoba? {
        return deti[name]
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

    //*************************************** Monada Optional
    fun diteO(name: String): Optional<Osoba> {
        return Optional.ofNullable(deti[name])
    }

    fun vnukO(dite: String, vnuk: String): Optional<Osoba> {
        return diteO(dite).flatMap { diteO(vnuk) }
    }

    fun praVnukO(dite: String, vnuk: String, pravnuk: String): Optional<Osoba> {
        return vnukO(dite, vnuk).flatMap { diteO(pravnuk) }
    }

    //*************************************** Monada Either
    fun diteE(_name: String): Either<String, Osoba> =
        deti[_name]?.let {
            Either.Right(it)
        } ?: Either.Left("$name nema dite: $_name")


    fun vnukE(dite: String, vnuk: String): Either<String, Osoba> =
        this.diteE(dite).flatMap { diteE(vnuk) }


    fun praVnukE(dite: String, vnuk: String, pravnuk: String): Either<String, Osoba> =
        diteE(dite).flatMap { diteE(vnuk) }.flatMap { diteE(pravnuk) }


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
        return "$name${if (deti.isEmpty()) "" else ": " + deti.values}"
    }

    fun print(n: Int) {
        println(" ".repeat(n) + name)
        deti().forEach { it.print(n + name.length) }
    }
}

fun Osoba.potomek(name: String, potomci: (Osoba.() -> Unit)? = null) {
    val potomek = Osoba(name)
    val predek = this
    predek.addDite(potomek)
    potomci?.invoke(potomek) // if (potomci!=null) potomek.potomci()
}

fun main() {
    val praded = Osoba("praded").apply {
        potomek("ded") {
            potomek("otec") {
                potomek("vnuk1")
                potomek("vnuk2")
            }
            potomek("matka") {

            }
        }
        potomek("babi") {
            potomek("otec2")
        }
    }
    println(praded.diteE("ded"))
    println(praded.vnukE("ded", "otec"))
    println(praded.praVnukE("ded", "otec", "vnuk1"))
    println(praded.praVnukE("ded", "otec", "vnuk"))

    praded.print(0)
    println("praded = $praded")
    println("praded.vnuci: ${praded.vnuci().map { it.name }}")
    println("praded.pravnuci: ${praded.pravnuci().map { it.name }}")
    println(praded.pravnuk("ded", "otec", "vnuk1"))
}
