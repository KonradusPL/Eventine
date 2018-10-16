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

        mView.setUserData(fullName)
    }

    fun onDetach(){
        mCompositeDisposable.clear()
    }
}