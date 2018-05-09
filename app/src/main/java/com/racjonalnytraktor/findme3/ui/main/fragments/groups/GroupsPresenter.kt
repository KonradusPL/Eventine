package com.racjonalnytraktor.findme3.ui.main.fragments.groups

import com.racjonalnytraktor.findme3.data.repository.groups.GroupsRepositoryImpl
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.SchedulerProvider


class GroupsPresenter<V: GroupsMvp.View>: BasePresenter<V>(), GroupsMvp.Presenter<V> {

    val repo = GroupsRepositoryImpl()

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        compositeDisposable.add(repo.getGroups()
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe {item ->
                    view.updateGroupsList(item)
                })
        compositeDisposable.add(repo.getTasks()
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe {item ->
                    view.updateTasksList(item)
                })
    }
}