package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.repository.groups.GroupsRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.ui.base.MvpView
import com.racjonalnytraktor.findme3.utils.SchedulerProvider


class FeedPresenter<V: FeedMvp.View>: BasePresenter<V>(), FeedMvp.Presenter<V> {

    val repo = GroupsRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        repo.onAttatch(mvpView.getCtx())

        view.showGroupsLoading()

        compositeDisposable.add(repo.getGroups()
                .flatMapIterable { t -> t }
                .subscribe({group: Group? ->
                    view.hideGroupsLoading()
                    view.updateGroupsList(group!!)
                },{t: Throwable? ->
                    view.hideGroupsLoading()
                    view.showMessage(t!!.localizedMessage,MvpView.MessageType.ERROR)
                }))
        /*compositeDisposable.add(repo.getTasks()
                .flatMapIterable { t -> t }
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe {item ->
                    view.updateTasksList(item)
                })*/
    }

    override fun onGroupItemClick(groupName: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}