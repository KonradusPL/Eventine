package com.racjonalnytraktor.findme3.ui.register

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.jakewharton.rxbinding2.view.RxView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.login.LoginActivity
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : RegisterMvp.View, BaseActivity() {

    lateinit var mPresenter: RegisterPresenter<RegisterMvp.View>

    val callbackManager = CallbackManager.Factory.create()
    private lateinit var mLoginFb: LoginButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mPresenter = RegisterPresenter()
        mPresenter.onAttach(this)

        progressBar = progressRegister

        registerFbLoginCallback()

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
        RxView.clicks(buttonSignIn)
                .subscribe {
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
        RxView.clicks(buttonFbLogin)
                .subscribe {
                    mLoginFb.performClick()
                }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }

    override fun showLoginLoading() {
        progressRegister.visibility = View.VISIBLE
        progressRegister.isIndeterminate = true
        fieldEmail.isEnabled = false
        fieldPassword.isEnabled = false
        buttonFbLogin.isEnabled = false
        buttonRegister.isEnabled = false
    }

    override fun hideLoginLoading() {
        progressRegister.isIndeterminate = false
        progressRegister.visibility = View.INVISIBLE
        fieldEmail.isEnabled = true
        fieldPassword.isEnabled = true
        buttonRegister.isEnabled = true
        buttonFbLogin.isEnabled = true
    }

    private fun registerFbLoginCallback(){
        mLoginFb = LoginButton(this)
        mLoginFb.setReadPermissions(arrayListOf("user_friends"))
        mLoginFb.registerCallback(callbackManager,object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                mPresenter.onFacebookLoginSuccess(result)
            }
            override fun onCancel() {
            }
            override fun onError(error: FacebookException?) {
                Log.d("error",error!!.message)
            } })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode,resultCode,data)
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
