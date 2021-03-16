package com.jackson.repositoriestest

import android.app.Application
import com.jackson.repositoriestest.di.apiModule
import com.jackson.repositoriestest.di.networkModule
import com.jackson.repositoriestest.di.viewModelModule
import org.koin.android.ext.android.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(
            networkModule,
            apiModule,
            viewModelModule
        ))
    }

}