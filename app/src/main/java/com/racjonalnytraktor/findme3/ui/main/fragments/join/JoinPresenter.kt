package com.racjonalnytraktor.findme3.ui.main.fragments.join

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.data.repository.join.JoinRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import com.racjonalnytraktor.findme3.utils.StringHelper

class JoinPresenter<V: JoinMvp.View>: BasePresenter<V>(),JoinMvp.Presenter<V> {

    val repo = JoinRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        repo.onAttatch(mvpView.getCtx())

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
       view.showJoinLoading()
        compositeDisposable.add(repo.joinGroup(groupName)
                .subscribe({response: String? ->
                    repo.prefs.setCurrentGroupId(response.orEmpty())
                    repo.prefs.setCurrentGroupName(groupName)
                    view.hideJoinLoading()
                    view.showMessage("Udało się !",MvpView.MessageType.SUCCESS)
                    view.openMapActivity()
                },{throwable: Throwable? ->
                    view.hideJoinLoading()
                    val errorCode = StringHelper.getErrorCode(throwable?.localizedMessage.orEmpty())
                    Log.d("error1",throwable.toString())
                    Log.d("error2",errorCode)
                    if(errorCode == "401")
                        view.showMessage("Taki event istnieje",MvpView.MessageType.ERROR)
                }))
    }

    override fun onAcceptInvitationClick(groupId: String) {
        compositeDisposable.add(repo.acceptInvitation(groupId)
                .subscribe({t: String? ->
                },{t: Throwable? ->
                    Log.d("eropr",t!!.message)
                }))
    }
}