package com.example.paypals.services

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.example.paypals.data.Contact
import io.reactivex.Observable

class LocalContactsService(private val contentResolver: ContentResolver) {

    fun readContactsFromPhoneBook(): Observable<List<Contact>> {
        val contacts = mutableListOf<Contact>()
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        cursor?.run {
            while (moveToNext()) {
                val name = getDataForColumn(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val phoneNumber = getDataForColumn(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val avatarUrl = getDataForColumn(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

                contacts.add(Contact(
                    name = name,
                    number = phoneNumber ?: Contact.generateRandomNumber(),
                    avatarUrl = avatarUrl
                ))
            }
            close()
            return Observable.just(contacts.toList().distinctBy { it.name })
        }
        return Observable.error(Throwable("Didn't receive contacts from phone book"))
    }

    private fun Cursor.getDataForColumn(column: String) = getString(getColumnIndex(column))
}