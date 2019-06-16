package com.racjonalnytraktor.findme3.ui.main.fragments.join

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.data.repository.join.JoinRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import com.racjonalnytraktor.findme3.utils.StringHelper

class JoinPresenter<V: JoinMvp.View>: BasePresenter<V>(),JoinMvp.Presenter<V> {

    private val TAG = "JoinPresenter"

    val repo = JoinRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        view.showInvitesLoading()

        compositeDisposable.add(repo.getInvitations()
                .map { t -> t.invitations }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .doOnComplete{
                    Log.d("bbbbb","b123")
                    Log.d("poipoipoi","poipoipoi")
                    view.hideInvitesLoading()
                }
                .subscribe({t: Invitation? ->
                    view.updateList(t!!)
                    view.hideInvitesLoading()
                },{t: Throwable? ->
                    view.hideInvitesLoading()
                    Log.d("error",t!!.message)
                }))
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.dispose()
    }

   override fun onJoinGroupClick(groupName: String) {
       view.hideJoinLoading()
       /*view.showJoinLoading()
        compositeDisposable.add(repo.joinGroup(groupName)
                .subscribe({response: String? ->
                    Log.d(TAG,"onJoinGroupClick: $response")
                    repo.prefs.apply {
                        setCurrentGroupId(response.orEmpty())
                        setCurrentGroupName(groupName)
                    }
                    view.hideJoinLoading()
                    view.showMessage("Udało się !",MvpView.MessageType.SUCCESS)
                    view.openMapActivity()
                },{error: Throwable? ->
                    Log.d(TAG,"onJoinGroupClick: ${error.toString()}")
                    view.hideJoinLoading()
                    val errorCode = StringHelper.getErrorCode(error?.localizedMessage.orEmpty())
                    if(errorCode == "401")
                        view.showMessage("Taki event istnieje",MvpView.MessageType.ERROR)
                }))*/
    }

    override fun onAcceptInvitationClick(invitation: Invitation) {
        compositeDisposable.add(repo.acceptInvitation(invitation.id)
                .subscribe({t: String? ->
                    repo.prefs.apply {
                        setCurrentGroupId(invitation.id)
                        setCurrentGroupName(invitation.groupName)
                    }

                    view.showMessage("Witamy w ${invitation.groupName} !",MvpView.MessageType.INFO)
                    view.openMapActivity()
                },{t: Throwable? ->
                    view.showMessage("Dołączenie nie powiodło się",MvpView.MessageType.ERROR)
                }))
    }
}