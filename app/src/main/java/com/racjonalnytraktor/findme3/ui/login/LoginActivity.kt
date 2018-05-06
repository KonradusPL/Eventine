package com.racjonalnytraktor.findme3.ui.login

import android.content.Intent
import android.os.Bundle
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.ui.base.BaseActivity
import com.racjonalnytraktor.findme3.ui.main.MainActivity

class LoginActivity : BaseActivity(),LoginMvp.View {

    lateinit var presenter: LoginPresenter<LoginMvp.View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = LoginPresenter()
    }

    override fun openMainActivity() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun openRegisterActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
