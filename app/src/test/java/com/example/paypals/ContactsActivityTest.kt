package com.example.paypals

import android.os.Build
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.paypals.data.Contact
import com.example.paypals.store.FetchContactsSuccess
import com.example.paypals.store.Init
import com.example.paypals.store.SelectContact
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class ContactsActivityTest {

    private lateinit var activity: ContactsActivity

    @Before
    fun setup() {
        activity = buildActivity(ContactsActivity::class.java).setup().get()
    }


    @Test
    fun `The contacts list should render as many contacts as there are in the state`() {
        // Given
        val recyclerView = activity.findViewById<RecyclerView>(R.id.contactsRecyclerView)

        // When
        store.dispatch(FetchContactsSuccess(listOf(
            Contact("", "", ""),
            Contact("", "", ""),
            Contact("", "", ""),
            Contact("", "", "")
        )))

        // Then
        assertEquals(4, recyclerView.childCount)
    }

    @Test
    fun `The fab should not show when no contacts have been selected`() {
        // Given
        val fab = activity.findViewById<FloatingActionButton>(R.id.contactsFab)

        // When
        store.dispatch(Init())

        // Then
        assertFalse(fab.isVisible)
    }

    @Test
    fun `The fab should only show when contacts have been selected`() {
        // Given
        val fab = activity.findViewById<FloatingActionButton>(R.id.contactsFab)

        // When
        store.dispatch(SelectContact(Contact("", "", "")))

        // Then
        assertTrue(fab.isVisible)
    }
}