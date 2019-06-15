package com.racjonalnytraktor.findme3.injection.modules

import com.racjonalnytraktor.findme3.data.local.Preferences
import com.racjonalnytraktor.findme3.data.local.SharedPreferences
import dagger.Binds
import dagger.Module

@Module
abstract class PreferencesModule {
    @Binds
    abstract fun bindPreferences(prefs: SharedPreferences): Preferences
}