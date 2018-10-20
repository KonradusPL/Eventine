package com.racjonalnytraktor.findme3.ui.map.fragments.profile

import com.racjonalnytraktor.findme3.data.repository.BaseRepository
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

    fun onDetach(){
        mCompositeDisposable.clear()
    }
}