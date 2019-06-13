package com.example.paypals.util

import android.view.View
import com.brianegan.bansa.Store
import com.brianegan.bansa.Subscription
import com.example.paypals.store.ApplicationState
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun Store<ApplicationState>.subscribe(
    block: (state: ApplicationState, previousState: ApplicationState) -> Unit
): Subscription {
    var previousState = ApplicationState()
    return subscribe { state ->
        block(state, previousState)
        previousState = state
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun String.cleanAndParseCurrencyFormat(): BigDecimal {
    val cleaned = replace("[€,.]".toRegex(), "")
    return BigDecimal(cleaned)
        .setScale(2, BigDecimal.ROUND_FLOOR)
        .divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)
}

fun BigDecimal.toCurrencyFormat(): String = NumberFormat
    .getCurrencyInstance(Locale.UK)
    .format(this)
    .replace("£", "€")
