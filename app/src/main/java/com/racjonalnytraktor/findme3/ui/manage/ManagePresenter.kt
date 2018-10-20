package com.racjonalnytraktor.findme3.ui.manage

import android.util.Log
import com.racjonalnytraktor.findme3.data.network.model.ChangeSubGroupRequest
import com.racjonalnytraktor.findme3.data.network.model.changegroups.Typed
import com.racjonalnytraktor.findme3.data.repository.ManageRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.StringHelper


class ManagePresenter<V: ManageMvp.View>: BasePresenter<V>(),ManageMvp.Presenter<V> {

    val repo = ManageRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        getPeopleInGroups()
    }

    fun getPeopleInGroups(){
        view.showLoading()

        compositeDisposable.add(repo.getPeopleInGroups()
                .doOnComplete { view.hideLoading() }
                .subscribe({t: Typed? ->
                    view.updateList(t!!)
                    Log.d("typed",t!!.type)
                },{t: Throwable? ->
                    Log.d("getPeopleInGroups",t.toString())
                    view.hideLoading()
                    view.showError()
                }))
    }

    override fun onGroupChanged(changedGroup: String, changingId: String){
        val request = ChangeSubGroupRequest(changingId, repo.prefs.getCurrentGroupId(),changedGroup)
        compositeDisposable.add(repo.changeSubGroups(request)
                .subscribe({t: String? ->
                    view.showMessage("Zapisano",MvpView.MessageType.INFO)
                },{t: Throwable? ->
                    if(StringHelper.getErrorCode(t!!.localizedMessage) == "403")
                        view.showMessage("Brak uprawnień",MvpView.MessageType.INFO)
                }))
    }
}