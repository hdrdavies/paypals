package com.example.paypals.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class HelpersTest {
    @Test
    fun `cleanAndParseCurrencyFormat should convert a euro currency string to a rounded big decimal`() {
        assertEquals(BigDecimal(1000.00).setScale(2, BigDecimal.ROUND_FLOOR), "€1000.00".cleanAndParseCurrencyFormat())
        assertEquals(BigDecimal(34.32).setScale(2, BigDecimal.ROUND_FLOOR), "€34.32".cleanAndParseCurrencyFormat())
    }

    @Test
    fun `toCurrencyFormat should convert a BigDecimal to a euro currency string`() {
        assertEquals(BigDecimal(34.32).toCurrencyFormat(), "€34.32")
        assertEquals(BigDecimal(920.40).toCurrencyFormat(), "€920.40")
    }
}