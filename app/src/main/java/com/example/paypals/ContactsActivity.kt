package com.example.paypals

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.brianegan.bansa.Subscription
import com.example.paypals.data.Contact
import com.example.paypals.middleware.ContactsMiddleware
import com.example.paypals.services.LocalContactsService
import com.example.paypals.store.ApplicationState
import com.example.paypals.ui.ContactsItemDetailsLookup
import com.example.paypals.ui.ContactsListAdapter
import com.example.paypals.util.gone
import com.example.paypals.util.subscribe
import com.example.paypals.util.visible
import kotlinx.android.synthetic.main.activity_contacts.*
import org.jetbrains.anko.toast

class ContactsActivity : AppActivity() {

    private var storeSubscription: Subscription? = null
    private val readContactsPermissionRequest = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        setTitle(R.string.contacts)

        if (hasReadContactsPermission()) {
            getContacts()
        } else {
            requestPermissionToReadContacts()
        }

        contactsFab.setOnClickListener {
            startActivity(Intent(this, AmountActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        storeSubscription = store.subscribe { state, previousState ->
            state.onChange(previousState)
        }
    }

    override fun onPause() {
        super.onPause()
        storeSubscription?.unsubscribe()
    }

    private fun ApplicationState.onChange(previousState: ApplicationState) {
        if (hasFetchedContacts && hasFetchedContacts != previousState.hasFetchedContacts) {
            createContactsList(contacts)
        }
        if (hasFetchContactsError) {
            toast("error fetching contacts, check your connection")
        }

        if (selectedContacts.isEmpty()) contactsFab.gone() else contactsFab.visible()
    }

    private fun createContactsList(contacts: List<Contact>) {
        contactsRecyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ContactsListAdapter(contacts)

        contactsRecyclerView.adapter = adapter

        adapter.notifyDataSetChanged()
        adapter.tracker = SelectionTracker.Builder<Long>(
            "contactSelection",
            contactsRecyclerView,
            StableIdKeyProvider(contactsRecyclerView),
            ContactsItemDetailsLookup(contactsRecyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        )
            .build()
    }

    private fun requestPermissionToReadContacts() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), readContactsPermissionRequest)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == readContactsPermissionRequest) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            } else {
                toast("This app needs to read contacts!")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun hasReadContactsPermission() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    private fun getContacts() = ContactsMiddleware(LocalContactsService(contentResolver)).getContacts()
}

