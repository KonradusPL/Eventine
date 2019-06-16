package com.racjonalnytraktor.findme3.ui.login

import android.util.Log
import com.facebook.login.LoginResult
import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbResponse
import com.racjonalnytraktor.findme3.data.repository.LoginRepository
import com.racjonalnytraktor.findme3.data.repository.UserRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.StringHelper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class LoginPresenter<V: LoginMvp.View>: BasePresenter<V>(), LoginMvp.Presenter<V> {

    private val TAG = "LoginPresenter"

    val repo = LoginRepository()
    val userRepo = UserRepository()

    override fun onEmailLoginClick(email: String, password: String) {
        view.showLoginLoading()

        compositeDisposable.add(repo.loginWithEmail(LoginRequest(email, password))
                .subscribe({response: LoginResponse? ->
                    Log.d("loginWithEmail","isPartner: ${response?.isPartner}")
                    Log.d("logowanie","działa")
                    Log.d("logtok",response?.token)
                    repo.prefs.createUser(User(fullName = response!!.fullName))
                    repo.saveUser(response.token,"",email,response.isPartner)

                    onLoginSuccess()

                },{throwable: Throwable? ->
                    Log.d("logowanie","error: ${throwable.toString()}")
                    view.hideLoginLoading()
                    val errorCode = StringHelper.getErrorCode(throwable!!.localizedMessage)
                    Log.d("errorCodeaaa",errorCode)
                    if(errorCode == "401")
                        view.showMessage("Błędne dane logowania",MvpView.MessageType.ERROR)
                }))
    }

    private fun onLoginSuccess(){
        view.hideLoginLoading()
        view.showMessage("Sukces!",MvpView.MessageType.SUCCESS)
        Log.d(TAG,"onLoginSuccess")

        compositeDisposable.add(userRepo.getGroupList()
                .subscribe({groups: List<Group> ->
                    Log.d(TAG,"onLoginSuccess ${groups}")
                    if (groups.isEmpty())
                        view.openMainActivity()
                    else{
                        val id = groups.first().id
                        val groupName = groups.first().groupName
                        repo.prefs.apply {
                            setIsUserInGroup(true)
                            setCurrentGroupId(id)
                            setCurrentGroupName(groupName)
                        }
                        view.openMapActivity()
                    }
        },{t: Throwable? ->
                    Log.d(TAG,"onLoginSuccess ${t.toString()}")
                }
        ))
    }

    override fun onFacebookLoginClick() {
    }

    override fun onGoogleLoginClick() {
    }

    override fun onFacebookLoginSuccess(loginResult: LoginResult?) {
        view.showLoginLoading()

        repo.getUserInfo()
                .flatMap { user: User -> repo.registerByFacebook(user) }
                .subscribe({t: RegisterFbResponse? ->
                    view.hideLoginLoading()
                    Log.d("onFacebookLoginSuccess","${t!!.fbId} ${t.token}")
                    repo.saveUser(t!!.token,t.fbId,"",false)
                    view.openMainActivity()},
                        { throwable: Throwable? ->
                    view.hideLoginLoading()
                    Log.d("error",throwable.toString())
                })
    }


    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }

}