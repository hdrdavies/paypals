package com.example.paypals.middleware

import com.brianegan.bansa.BaseStore
import com.example.paypals.data.Contact
import com.example.paypals.services.LocalContactsService
import com.example.paypals.services.RemoteContactsService
import com.example.paypals.store.ApplicationState
import com.example.paypals.store.FetchContactsFailure
import com.example.paypals.store.FetchContactsSuccess
import com.example.paypals.store.FetchingContacts
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test


class ContactsMiddlewareTest {

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun `getContacts should dispatch a list of contacts to the store if both calls are successful`() {
        val testContact1 = Contact("aaa", "b", "c")
        val testContact2 = Contact("ddd", "e", "f")
        val testContact3 = Contact("bbb", "e", "f")

        val mockRemoteContactsService = mockk<RemoteContactsService>()

        every {
            mockRemoteContactsService.fetchContacts()
        } returns Observable.just(listOf(testContact1, testContact3))

        val mockLocalContactsService = mockk<LocalContactsService>()

        every {
            mockLocalContactsService.readContactsFromPhoneBook()
        } returns Observable.just(listOf(testContact2))

        val mockStore = mockk<BaseStore<ApplicationState>>()
        every { mockStore.dispatch(any()) } returns ApplicationState()

        ContactsMiddleware(
            remoteContactsService = mockRemoteContactsService,
            localContactsService = mockLocalContactsService,
            appStore = mockStore
        ).getContacts()

        verify(exactly = 1) { mockStore.dispatch(ofType(FetchingContacts::class)) }
        verify(exactly = 1) { mockStore.dispatch(ofType(FetchContactsSuccess::class)) }
    }

    @Test
    fun `getContacts should dispatch an error to the store if a call is unsuccessful`() {
        val workingRemoteService = mockk<RemoteContactsService>()
        every { workingRemoteService.fetchContacts() } returns Observable.just(emptyList())

        val brokenLocalService = mockk<LocalContactsService>()
        every {
            brokenLocalService.readContactsFromPhoneBook()
        } returns Observable.error(Throwable("error!"))

        val mockStore = mockk<BaseStore<ApplicationState>>()
        every { mockStore.dispatch(any()) } returns ApplicationState()

        ContactsMiddleware(
            remoteContactsService = workingRemoteService,
            localContactsService = brokenLocalService,
            appStore = mockStore
        ).getContacts()

        verify(exactly = 1) { mockStore.dispatch(ofType(FetchingContacts::class)) }
        verify(exactly = 1) { mockStore.dispatch(ofType(FetchContactsFailure::class)) }
    }
}