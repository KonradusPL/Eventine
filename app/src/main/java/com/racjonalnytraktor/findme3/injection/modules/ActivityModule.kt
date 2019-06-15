package com.racjonalnytraktor.findme3.injection.modules

import android.app.Application
import android.content.Context
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.login.LoginMvp
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindLoginView(loginActivity: LoginActivity): LoginMvp.View
}