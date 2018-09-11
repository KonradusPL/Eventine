package com.racjonalnytraktor.findme3.ui.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.racjonalnytraktor.findme3.TestActivity
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import com.racjonalnytraktor.findme3.ui.map.MapActivity
import com.racjonalnytraktor.findme3.utils.StringHelper

class SplashActivity : BaseActivity(),SplashMvp.View {

    lateinit var presenter: SplashPresenter<SplashMvp.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = SplashPresenter()
        presenter.onAttach(this)
    }

    override fun openMainActivity() {
        //Logowanie -> Mapa
        startActivity(Intent(this,MapActivity::class.java))
        finish()
    }

    override fun openLoginActivity() {
        startActivity(Intent(this,MapActivity::class.java))
        finish()
    }
}