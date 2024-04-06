package com.example.musicplayerkts.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.musicplayerkts.di.*
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(
                listOf(
                    featureStag,
                    networkModule,
                    serviceModule
                )
            )
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}