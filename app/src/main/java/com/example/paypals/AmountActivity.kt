package com.example.paypals

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.brianegan.bansa.Subscription
import com.example.paypals.store.SetAmount
import com.example.paypals.util.cleanAndParseCurrencyFormat
import com.example.paypals.util.gone
import com.example.paypals.util.toCurrencyFormat
import com.example.paypals.util.visible
import kotlinx.android.synthetic.main.activity_amount.*


class AmountActivity : AppActivity() {

    private var storeSubscription: Subscription? = null
    private val maxAmount = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amount)
        setTitle(R.string.enter_amount)

        amountInput.setup()

        amountFab.setOnClickListener {
            startActivity(Intent(this, ConfirmationActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        storeSubscription = store.subscribe {
            if (it.amount > 0 && it.amount <= maxAmount) {
                amountFab.visible()
            } else {
                amountFab.gone()
            }
            if (it.amount > maxAmount) {
                amountInput.error =  getString(R.string.max_amount_is_x, maxAmount)
                vibrate()
            } else {
                amountInput.error = null
            }
        }
    }

    override fun onPause() {
        super.onPause()
        storeSubscription?.unsubscribe()
    }

    private fun EditText.setup() {
        var amountInputString = ""
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != amountInputString){
                    removeTextChangedListener(this)
                    val parsed = s.toString().cleanAndParseCurrencyFormat()

                    store.dispatch(SetAmount(parsed.toDouble()))

                    val formatted = parsed.toCurrencyFormat()

                    amountInputString = formatted
                    setText(formatted)
                    setSelection(formatted.length)
                    addTextChangedListener(this)
                }
            }
        })
    }

    private fun vibrate() {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }
}