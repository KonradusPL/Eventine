package com.konradpekala.blefik.injection.component

import android.app.Application

import com.racjonalnytraktor.findme3.injection.modules.*
import com.racjonalnytraktor.findme3.ui.EventineApplication
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [
    ActivityModule::class,
    FragmentModule::class,
    AuthModule::class,
    PreferencesModule::class,
    RepositoryModule::class,
    UtilsModule::class
])
@Singleton
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }

    fun inject(loginActivity: LoginActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(eventineApplication: EventineApplication)

}