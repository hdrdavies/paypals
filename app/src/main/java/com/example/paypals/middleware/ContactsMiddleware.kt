package com.example.paypals.middleware

import android.annotation.SuppressLint
import com.brianegan.bansa.Store
import com.example.paypals.data.Contact
import com.example.paypals.services.LocalContactsService
import com.example.paypals.services.RemoteContactsService
import com.example.paypals.store
import com.example.paypals.store.ApplicationState
import com.example.paypals.store.FetchContactsFailure
import com.example.paypals.store.FetchContactsSuccess
import com.example.paypals.store.FetchingContacts
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class ContactsMiddleware(
    private val localContactsService: LocalContactsService,
    private val remoteContactsService: RemoteContactsService = RemoteContactsService(),
    private val appStore: Store<ApplicationState> = store
) {
    @SuppressLint("CheckResult")
    fun getContacts() {
        appStore.dispatch(FetchingContacts())
        Observable.zip(
            remoteContactsService.fetchContacts(),
            localContactsService.readContactsFromPhoneBook(),
            BiFunction<List<Contact>, List<Contact>, List<Contact>> { a, b -> a + b }
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ contacts ->
                appStore.dispatch(FetchContactsSuccess(contacts.sortedBy { it.name }))
            }, {
                appStore.dispatch(FetchContactsFailure(it.message ?: ""))
            })
    }
}