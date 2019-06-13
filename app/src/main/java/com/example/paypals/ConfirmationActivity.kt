package com.example.paypals

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.example.paypals.data.Contact
import com.example.paypals.util.toCurrencyFormat
import kotlinx.android.synthetic.main.activity_confirmation.*

class ConfirmationActivity : AppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        setTitle(R.string.confirmation)

        store.state.run {
            val total = amount.toBigDecimal()
            val amountEach = (total / selectedContacts.size.toBigDecimal()).toCurrencyFormat()
            totalAmount.text = total.toCurrencyFormat()
            renderRecipients(
                recipients = selectedContacts,
                amountEach = amountEach
            )
        }
    }

    private fun renderRecipients(recipients: Set<Contact>, amountEach: String) {
        val inflater = LayoutInflater.from(this)
        recipients.forEach {
            val recipientRow = inflater.inflate(R.layout.recipient_row, recipientsContainer, false)
            recipientRow.findViewById<TextView>(R.id.recipientName).text = it.name.split(" ").first()
            recipientRow.findViewById<TextView>(R.id.recipientAmount).text = amountEach
            recipientsContainer.addView(recipientRow)
        }
    }
}