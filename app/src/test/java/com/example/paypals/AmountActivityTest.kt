package com.example.paypals

import android.os.Build
import android.widget.EditText
import com.example.paypals.store.SetAmount
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class AmountActivityTest {

    private lateinit var activity: AmountActivity

    @Before
    fun setup() {
        activity = buildActivity(AmountActivity::class.java).setup().get()
    }

    @Test
    fun `The amount input should show an error when the total is above the max`() {
        // Given
        val amountInput = activity.findViewById<EditText>(R.id.amountInput)

        // When
        store.dispatch(SetAmount(10000.00))

        // Then
        assertNotNull(amountInput.error)
    }
}