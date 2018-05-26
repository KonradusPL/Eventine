package com.racjonalnytraktor.findme3.ui.login

import android.content.Intent
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.jakewharton.rxbinding2.view.RxView
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import com.racjonalnytraktor.findme3.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(),LoginMvp.View {

    lateinit var presenter: LoginPresenter<LoginMvp.View>

    val callbackManager = CallbackManager.Factory.create()
    private lateinit var mLoginFb: LoginButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter()
        presenter.onAttach(this)

        registerFbLoginCallback()

        observeViews()

    }

    private fun observeViews(){
        RxView.clicks(buttonLogin)
                .subscribe {
                    presenter.onEmailLoginClick(fieldEmail.text.toString(),fieldPassword.text.toString())
                }
        RxView.clicks(buttonFbLogin)
                .subscribe {
                    mLoginFb.performClick()
                }
        RxView.clicks(buttonNewAccount)
                .subscribe {
                    openRegisterActivity()
                }
    }

    private fun registerFbLoginCallback(){
        mLoginFb = LoginButton(this)
        mLoginFb.registerCallback(callbackManager,object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                presenter.onFacebookLoginSuccess(result)
            }
            override fun onCancel() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onError(error: FacebookException?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            } })
    }

    override fun openMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun openRegisterActivity() {
        startActivity(Intent(this,RegisterActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode,resultCode,data)
    }
}
