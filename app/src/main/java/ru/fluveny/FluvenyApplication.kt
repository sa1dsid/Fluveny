package ru.fluveny

import android.app.Application
import ru.fluveny.di.AppContainer

class FluvenyApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
