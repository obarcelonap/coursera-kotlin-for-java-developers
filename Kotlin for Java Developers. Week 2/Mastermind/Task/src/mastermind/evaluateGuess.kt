package mastermind

import java.lang.Integer.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val rightPosition = countInRightPosition(secret, guess)
    val wrongPosition = sumMinimumCodeMatches(secret, guess) - rightPosition

    return Evaluation(rightPosition, wrongPosition)
}

fun countInRightPosition(secret: String, guess: String): Int =
    (secret zip guess).count { (s, g) -> s == g }

fun sumMinimumCodeMatches(secret: String, guess: String): Int =
    "ABCDEF".sumBy { code ->
        min(
            secret.count { it == code },
            guess.count { it == code }
        )
    }
