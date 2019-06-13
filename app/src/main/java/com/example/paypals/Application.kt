package com.example.paypals

import com.brianegan.bansa.BaseStore
import com.example.paypals.store.ApplicationState
import com.example.paypals.store.Init
import com.example.paypals.store.applicationReducer

class Application : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        store.dispatch(Init())
    }
}

val store = BaseStore(ApplicationState(), applicationReducer)
