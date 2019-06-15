package com.racjonalnytraktor.findme3.injection.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    fun application() = mApplication

    @Provides
    fun provideContext(): Context = mApplication

}