package com.example.paypals.store

import com.brianegan.bansa.Reducer

val applicationReducer = Reducer<ApplicationState> { state, action ->
    state.run {
        when (action) {
            is Init -> ApplicationState()
            is FetchingContacts -> copy(isFetchingContacts = true)
            is FetchContactsSuccess -> copy(
                isFetchingContacts = false,
                hasFetchedContacts = true,
                contacts = action.contacts
            )
            is FetchContactsFailure -> copy(
                isFetchingContacts = false,
                hasFetchContactsError = true,
                fetchContactsError = action.error
            )
            is SetAmount -> copy(amount = action.amount)
            is SelectContact -> copy(selectedContacts = selectedContacts + action.contact)
            is DeselectContact -> copy(selectedContacts = selectedContacts - action.contact)
            else -> this
        }
    }
}