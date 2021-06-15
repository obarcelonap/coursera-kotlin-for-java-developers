package nicestring

fun String.isNice(): Boolean = listOf(
    containsNoneOf("bu", "ba", "be"),
    countVowels() >= 3,
    countDoubleLetters() != 0,
)
    .atLeast(2)

private fun String.containsNoneOf(vararg others: CharSequence): Boolean = others.none { this.contains(it) }
private fun String.countVowels() = count { it in "aeiou" }
private fun String.countDoubleLetters() = zipWithNext().count { it.first == it.second }
private fun List<Boolean>.atLeast(number: Int): Boolean = filter { it }.count() >= number


