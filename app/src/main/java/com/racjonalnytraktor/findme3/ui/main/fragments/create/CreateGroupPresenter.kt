package com.racjonalnytraktor.findme3.ui.main.fragments.create

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.network.model.CreateGroupRequest
import com.racjonalnytraktor.findme3.data.repository.create.CreateRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView

class CreateGroupPresenter<V: CreateGroupMvp.View>: BasePresenter<V>(),CreateGroupMvp.Presenter<V> {

    val repo = CreateRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        repo.onAttatch(view.getCtx())

        compositeDisposable.add(repo.getFriends()
                .subscribe({user: User? ->
                    view.updateList(user!!)
                    Log.d("user",user!!.facebookId)
                    Log.d("user",user.fullName)
                    Log.d("user",user.profileUri)
                   /*repo.getFriendWithPicture(user!!)
                            .subscribe({userWithPicture: User? ->
                                view.showMessage("friends loaded",MvpView.MessageType.SUCCESS)
                                view.updateList(userWithPicture!!)
                                Log.d("user",userWithPicture!!.facebookId)
                                Log.d("user",userWithPicture.fullName)
                                Log.d("user",userWithPicture.profileUri)
                            },{
                                error: Throwable? ->
                                view.showMessage("picture error",MvpView.MessageType.ERROR)
                            })*/

                },{error: Throwable? ->

                }))
    }

    override fun createEvent(groupName: String, friendsList: List<String>) {
        val request = CreateGroupRequest(groupName,friendsList)
        Log.d("plplpl",request.groupName)
        Log.d("plplpl",request.facebookIds.toString())
        view.showCreateGroupLoading()
        compositeDisposable.add(repo.createGroup(request)
                .subscribe({response: String? ->
                    view.hideCreateGroupLoading()
                    repo.prefs.setCurrentGroupId(response.orEmpty())
                    repo.prefs.setCurrentGroupName(groupName)
                    view.showMessage("Udało się stworzyć event",MvpView.MessageType.SUCCESS)
                    view.openMapActivity()
                    Log.d("response",response.orEmpty())
                }, {error: Throwable? ->
                    view.hideCreateGroupLoading()
                    view.showMessage("Nie udało się stworzyć eventu",MvpView.MessageType.ERROR)
                    Log.d("error",error!!.message)
                        }))
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }
}