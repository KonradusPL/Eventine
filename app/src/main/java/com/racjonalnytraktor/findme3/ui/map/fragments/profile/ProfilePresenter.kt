package com.racjonalnytraktor.findme3.ui.map.fragments.profile

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

        val fullName = mRepo.preferences.getUserFullName()

        var role = ""
        if(mRepo.appRepo.getMembers().size > 0) {
            for (member in mRepo.appRepo.getMembers())
                if (member.name == fullName){
                    role = member.subgroup
                    mRepo.preferences.setRole(role)
                }
        }
        else
            role = mRepo.preferences.getRole()

        mView.setUserData(fullName,role,mRepo.preferences.isPartner())
    }

    fun onKeeperHelpClick(){
        val token = mRepo.preferences.getUserToken()
        //val data = HashMap<String,String>()
        //data["groupId"] = mRepo.preferences.getCurrentGroupId()
        //Log.d("onHelpClick",data.toString())

        mCompositeDisposable.add(mRepo.rest.networkService.callCareTaker(token)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe({ t: String? ->
                    mView.showMessage("Wysłano prośbę do opiekuna!", MvpView.MessageType.SUCCESS)
                },{t: Throwable? ->
                    mView.showMessage("Nieudało się wysłać prośby do opiekuna", MvpView.MessageType.SUCCESS)
                }))
    }

    fun onDetach(){
        mCompositeDisposable.clear()
    }
}