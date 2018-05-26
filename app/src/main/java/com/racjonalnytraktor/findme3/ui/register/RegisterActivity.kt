package com.racjonalnytraktor.findme3.ui.register

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.racjonalnytraktor.findme3.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
