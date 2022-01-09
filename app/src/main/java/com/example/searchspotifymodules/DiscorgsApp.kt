package com.example.searchspotifymodules

import android.app.Application
import com.example.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DiscorgsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DiscorgsApp)
            modules(
                Modules.apiModule,
                Modules.viewModelModule,
                Modules.netModule,
                Modules.repositoryModule
            )
        }
    }

}