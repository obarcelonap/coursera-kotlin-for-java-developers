package rationals

import java.math.BigInteger

data class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {
    init {
        require(denominator != 0.toBigInteger()) { "Denominator can't be 0." }
    }

    companion object {
        fun sameDenominator(first: Rational, second: Rational): Pair<Rational, Rational> =
            Pair(first * second.denominator, second * first.denominator)
    }

    fun normalize(): Rational {
        val normalized = this / numerator.gcd(denominator)
        if (denominator.isNegative()) {
            return Rational(-normalized.numerator, -normalized.denominator)
        }
        return normalized
    }

    operator fun plus(increment: Rational): Rational =
        if (denominator == increment.denominator) Rational(numerator + increment.numerator, denominator)
        else {
            val (first, second) = sameDenominator(this, increment)
            first + second
        }

    operator fun minus(decrement: Rational): Rational =
        if (denominator == decrement.denominator) Rational(numerator - decrement.numerator, denominator)
        else {
            val (first, second) = sameDenominator(this, decrement)
            first - second
        }

    operator fun times(other: Rational): Rational = Rational(numerator * other.numerator, denominator * other.denominator)
    operator fun times(number: BigInteger): Rational = Rational(numerator * number, denominator * number)

    operator fun div(other: Rational): Rational = this * Rational(other.denominator, other.numerator)
    operator fun div(number: BigInteger): Rational = Rational(numerator / number, denominator / number)

    override fun compareTo(other: Rational): Int {
        val (first, second) = sameDenominator(this.normalize(), other.normalize())
        return first.numerator.compareTo(second.numerator)
    }

    operator fun unaryMinus(): Rational = Rational(-numerator, denominator)

    override fun toString(): String {
        val (numerator, denominator) = normalize()

        return if (denominator == 1.toBigInteger()) "$numerator"
        else "$numerator/$denominator"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rational

        val (numerator, denominator) = normalize()
        val (otherNumerator, otherDenominator) = other.normalize()
        if (numerator != otherNumerator) return false
        if (denominator != otherDenominator) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

}

fun Int.toRational(): Rational = Rational(this.toBigInteger(), 1.toBigInteger())
fun String.toRational(): Rational =
    if (contains("/")) {
        val (first, second) = split("/", limit = 2)
        Rational(first.toBigInteger(), second.toBigInteger())
    } else Rational(toBigInteger(), 1.toBigInteger())

infix fun Int.divBy(other: Int) = Rational(toBigInteger(), other.toBigInteger())
infix fun Long.divBy(other: Long) = Rational(toBigInteger(), other.toBigInteger())
infix fun BigInteger.divBy(other: BigInteger) = Rational(this, other)
fun BigInteger.isNegative(): Boolean = this < 0.toBigInteger()

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}
