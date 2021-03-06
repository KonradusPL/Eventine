package com.racjonalnytraktor.findme3.ui.login

import android.content.Intent
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
import com.racjonalnytraktor.findme3.ui.main.MainActivity
import com.racjonalnytraktor.findme3.ui.map.MapActivity
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

        initClickListeners()

    }

    private fun initClickListeners(){
        RxView.clicks(buttonLogin)
                .subscribe {
                    presenter.onEmailLoginClick(fieldEmail.text.toString(),fieldPassword.text.toString())
                }
        /*RxView.clicks(buttonFbLogin)
                .subscribe {
                    mLoginFb.performClick()
                }*/
        RxView.clicks(buttonNewAccount)
                .subscribe {
                    openRegisterActivity()
                }
    }

    private fun registerFbLoginCallback(){
        mLoginFb = LoginButton(this)
        mLoginFb.setReadPermissions(arrayListOf("user_friends"))
        mLoginFb.registerCallback(callbackManager,object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                presenter.onFacebookLoginSuccess(result)
            }
            override fun onCancel() {
            }
            override fun onError(error: FacebookException?) {
                Log.d("error",error!!.message)
            } })
    }

    override fun showLoginLoading() {
        progressBarLogin.visibility = View.VISIBLE
        progressBarLogin.isIndeterminate = true
        fieldEmail.isEnabled = false
        fieldPassword.isEnabled = false
        buttonLogin.isEnabled = false
    }

    override fun hideLoginLoading() {
        progressBarLogin.isIndeterminate = false
        progressBarLogin.visibility = View.INVISIBLE
        fieldEmail.isEnabled = true
        fieldPassword.isEnabled = true
        buttonLogin.isEnabled = true
    }

    override fun openMainActivity() {
        startActivity(Intent(this,MapActivity::class.java))
        finish()
    }

    override fun openRegisterActivity() {
        startActivity(Intent(this,RegisterActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode,resultCode,data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
