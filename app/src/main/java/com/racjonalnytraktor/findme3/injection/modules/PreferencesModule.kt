package com.racjonalnytraktor.findme3.injection.modules

import com.racjonalnytraktor.findme3.data.local.Prefs
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import dagger.Binds
import dagger.Module

@Module
abstract class PreferencesModule {
    @Binds
    abstract fun bindPreferences(prefs: SharedPrefs): Prefs
}