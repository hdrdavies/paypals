package com.example.paypals.store

import com.brianegan.bansa.Action
import com.example.paypals.data.Contact

class Init : Action

class FetchingContacts : Action

data class FetchContactsSuccess(val contacts: List<Contact>) : Action

data class FetchContactsFailure(val error: String) : Action

data class SetAmount(val amount: Double) : Action

data class SelectContact(val contact: Contact) : Action

data class DeselectContact(val contact: Contact) : Action
