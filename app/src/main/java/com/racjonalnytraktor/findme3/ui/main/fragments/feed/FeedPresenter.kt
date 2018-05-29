package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import com.racjonalnytraktor.findme3.data.repository.groups.GroupsRepositoryImpl
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.SchedulerProvider


class FeedPresenter<V: FeedMvp.View>: BasePresenter<V>(), FeedMvp.Presenter<V> {

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

    override fun onGroupItemClick(groupName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}