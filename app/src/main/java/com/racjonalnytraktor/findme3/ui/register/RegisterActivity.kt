package com.racjonalnytraktor.findme3.ui.register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : RegisterMvp.View, BaseActivity() {

    lateinit var mPresenter: RegisterPresenter<RegisterMvp.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mPresenter = RegisterPresenter()
        mPresenter.onAttach(this)

        setSupportActionBar(toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = progressRegister

        initClickListeners()
    }

    private fun initClickListeners() {
        RxView.clicks(buttonRegister)
                .subscribe {
                    mPresenter.onRegisterButtonClick(
                            fieldEmail.text.toString(),
                            fieldPassword.text.toString(),
                            fieldName.text.toString())
                }
    }

    override fun openMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onDetach()
    }
}
