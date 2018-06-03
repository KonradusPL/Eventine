package com.racjonalnytraktor.findme3.ui.main.fragments.join

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Invitation
import com.racjonalnytraktor.findme3.data.repository.join.JoinRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.SchedulerProvider

class JoinPresenter<V: JoinMvp.View>: BasePresenter<V>(),JoinMvp.Presenter<V> {

    val repo = JoinRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        repo.onAttatch(mvpView.getCtx())

        compositeDisposable.add(repo.getInvitations()
                .map { t -> t.invitations }
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({t: Invitation? ->
                    view.updateList(t!!)
                },{t: Throwable? ->
                    Log.d("error",t!!.message)
                }))
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.dispose()
    }

   /* override fun onJoinGroupClick(groupName: String) {
        compositeDisposable.add(repo.joinGroup(groupName)
                .subscribe({response: String? ->

                },{error: Throwable? ->

                }))
    }*/

    override fun onAcceptInvitationClick(groupId: String) {
        compositeDisposable.add(repo.acceptInvitation(groupId)
                .subscribe({t: String? ->
                },{t: Throwable? ->
                    Log.d("eropr",t!!.message)
                }))
    }
}