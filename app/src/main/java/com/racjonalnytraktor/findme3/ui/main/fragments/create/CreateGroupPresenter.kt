package com.racjonalnytraktor.findme3.ui.main.fragments.create

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.create.CreateRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

class CreateGroupPresenter<V: CreateGroupMvp.View>: BasePresenter<V>(),CreateGroupMvp.Presenter<V> {

    val repo = CreateRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        compositeDisposable.add(repo.getFriends()
                .subscribe({user: User? ->
                    repo.getFriendWithPicture(user!!)
                            .subscribe({userWithPicture: User? ->
                                view.showMessage("friends loaded",MvpView.MessageType.SUCCESS)
                                Log.d("user",userWithPicture!!.facebookId)
                                Log.d("user",userWithPicture.fullName)
                                Log.d("user",userWithPicture.profileUri)
                            },{
                                error: Throwable? ->
                                view.showMessage("picture error",MvpView.MessageType.ERROR)
                            })

                },{error: Throwable? ->
                    view.showMessage(error!!.message!!,MvpView.MessageType.ERROR)

                }))

    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
}