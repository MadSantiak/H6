import kotlin.math.roundToInt

/**
 * Helper function that allows us to format a double to decimal points as needed within Kotlin,
 * without having to rely on platform specificity.
 */
fun Double.roundToDecimals(decimals: Int): Double {
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this * dotAt).roundToInt()
    var result = (roundedValue / dotAt) + (roundedValue % dotAt).toDouble() / dotAt
    return result
}