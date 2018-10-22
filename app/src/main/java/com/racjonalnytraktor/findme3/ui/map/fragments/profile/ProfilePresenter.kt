package com.racjonalnytraktor.findme3.ui.map.fragments.profile

import android.util.Log
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class ProfilePresenter {
    val mRepo = BaseRepository()
    val mCompositeDisposable = CompositeDisposable()
    lateinit var mView: ProfileFragment

    fun onAttach(view: ProfileFragment){
        mView = view

        val fullName = mRepo.prefs.getUserFullName()

        var role = ""
        if(mRepo.appRepo.getMembers().size > 0) {
            for (member in mRepo.appRepo.getMembers())
                if (member.name == fullName){
                    role = member.subgroup
                    mRepo.prefs.setRole(role)
                }
        }
        else
            role = mRepo.prefs.getRole()

        mView.setUserData(fullName,role)
    }

    fun onKeeperHelpClick(){
        val token = mRepo.prefs.getUserToken()
        val data = HashMap<String,String>()
        data["groupId"] = mRepo.prefs.getCurrentGroupId()
        Log.d("onHelpClick",data.toString())

        mCompositeDisposable.add(mRepo.rest.networkService.sendPingToNearest(token, data)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ t: String? ->
                    mView.showMessage("Wysłano prośbę o pomoc!", MvpView.MessageType.SUCCESS)
                },{t: Throwable? ->
                    mView.showMessage("Nieudało się wysłać helpa", MvpView.MessageType.SUCCESS)
                }))
    }

    fun onDetach(){
        mCompositeDisposable.clear()
    }
}