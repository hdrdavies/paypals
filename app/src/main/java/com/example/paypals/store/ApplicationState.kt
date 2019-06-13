package com.example.paypals.store

import com.example.paypals.data.Contact

data class ApplicationState(
    val isFetchingContacts: Boolean = false,
    val hasFetchedContacts: Boolean = false,
    val hasFetchContactsError: Boolean = false,
    val fetchContactsError: String = "",
    val contacts: List<Contact> = emptyList(),
    val selectedContacts: Set<Contact> = emptySet(),
    val amount: Double = 0.0
)