package br.com.stone.plata.support

import java.text.NumberFormat
import java.util.*

/**
 * @author filpgame
 * @since 23/06/2017
 */
fun Double.asLong() = (this * 100).toLong()

fun Double.parseCentsToCurrency(locale: Locale): String {
    return NumberFormat.getCurrencyInstance(locale).format((this / 100))
}

fun Long.parseCentsToCurrency(locale: Locale): String {
    return NumberFormat.getCurrencyInstance(locale).format((this.toDouble() / 100))
}