package com.example.paypals.store

import com.example.paypals.data.Contact
import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationReducerTest {

    private val defaultState = ApplicationState()
    private val testContact = Contact("a", "b", "c")
    private val testContact2 = Contact("d", "e", "f")

    @Test
    fun `Init should initialise the store with the default Application state`() {
        assertEquals(defaultState, applicationReducer.reduce(
            ApplicationState(isFetchingContacts = true),
            Init()
        ))
    }

    @Test
    fun `FetchingContacts should return a loading state`() {
        assertEquals(
            ApplicationState(isFetchingContacts = true),
            applicationReducer.reduce(
                defaultState,
                FetchingContacts()
            )
        )
    }

    @Test
    fun `FetchingContacts should return a successful state with the contacts`() {
        assertEquals(
            ApplicationState(
                hasFetchedContacts = true,
                contacts = listOf(testContact, testContact)
            ),
            applicationReducer.reduce(
                defaultState,
                FetchContactsSuccess(listOf(testContact, testContact))
            )
        )
    }

    @Test
    fun `FetchContactsFailure should return an error state`() {
        assertEquals(
            ApplicationState(
                hasFetchContactsError = true,
                isFetchingContacts = false,
                fetchContactsError = "error"
            ),
            applicationReducer.reduce(defaultState, FetchContactsFailure("error"))
        )
    }

    @Test
    fun `SetAmount should return the state with the amount modified`() {
        assertEquals(
            ApplicationState(amount = 100.0),
            applicationReducer.reduce(defaultState, SetAmount(100.00))
        )
    }

    @Test
    fun `SelectContact should return the state with a contact added to the selected contacts list`() {
        assertEquals(
            ApplicationState(selectedContacts = setOf(testContact, testContact2)),
            applicationReducer.reduce(
                defaultState.copy(selectedContacts = setOf(testContact)),
                SelectContact(testContact2)
            )
        )
    }

    @Test
    fun `DeselectContact should return the state with a contact taken away from the selected contacts list`() {
        assertEquals(
            ApplicationState(selectedContacts = setOf(testContact)),
            applicationReducer.reduce(
                defaultState.copy(selectedContacts = setOf(testContact, testContact2)),
                DeselectContact(testContact2)
            )
        )
    }
}