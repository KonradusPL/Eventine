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

        view.showFriendsLoading()

        compositeDisposable.add(repo.getFriends()
                .doOnComplete {
                    view.hideFriendsLoading()
                }
                .subscribe({user: User? ->
                    view.updateList(user!!)
                    Log.d("user",user!!.facebookId)
                    Log.d("user",user.fullName)
                    Log.d("user",user.profileUri)

                },{error: Throwable? ->
                    view.hideFriendsLoading()
                    Log.d("error",error.toString())
                }))
    }

    fun getFriends(){
        view.showFriendsLoading()
        compositeDisposable.add(repo.getFriends()
                .doOnComplete{
                    view.hideFriendsLoading()
                }
                .subscribe({user: User? ->
                    view.updateList(user!!)

                },{error: Throwable? ->
                    view.hideFriendsLoading()
                    Log.d("error",error.toString())
                }))
    }

    override fun createEvent(groupName: String, friendsList: List<User>) {
        val facebookIds = ArrayList<String>()
        val normalIds = ArrayList<String>()
        for(friend in friendsList){
            if(friend.facebookId.isNotEmpty())
                facebookIds.add(friend.facebookId)
            else
                normalIds.add(friend.id)
        }
        val request = CreateGroupRequest(groupName,facebookIds,normalIds)
        Log.d("facebookIds",facebookIds.toString())
        Log.d("normalIds",normalIds.toString())
        view.showCreateGroupLoading()
        compositeDisposable.add(repo.createGroup(request)
                .subscribe({response: String? ->
                    view.hideCreateGroupLoading()
                    repo.prefs.setCurrentGroupId(response.orEmpty())
                    repo.prefs.setCurrentGroupName(groupName)
                    view.clearFriendsList()
                    view.showMessage("Udało się stworzyć event",MvpView.MessageType.SUCCESS)
                    view.openMapActivity()
                    Log.d("response",response.orEmpty())
                }, {error: Throwable? ->
                    view.clearFriendsList()
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