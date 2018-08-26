package com.racjonalnytraktor.findme3.ui.main.fragments.feed

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.GroupWithUsers
import com.racjonalnytraktor.findme3.data.repository.groups.GroupsRepository
import com.racjonalnytraktor.findme3.ui.base.BasePresenter


class FeedPresenter<V: FeedMvp.View>: BasePresenter<V>(), FeedMvp.Presenter<V> {

    val repo = GroupsRepository

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        repo.onAttach(view.getCtx())

        view.showGroupsLoading()
        view.showTasksLoading()

        compositeDisposable.add(repo.getGroups()
                .doOnComplete{
                    view.hideGroupsLoading()
                    Log.d("getGroups","complete")
                }
                .subscribe({group: GroupWithUsers? ->
                    Log.d("getGroups","next")
                    view.updateGroupsList(group!!)
                },{t: Throwable? ->
                    Log.d("getGroups","error")
                    view.hideGroupsLoading()
                }))
        compositeDisposable.add(repo.getTasks()
                .doOnComplete{
                    Log.d("getTasks","complete")
                    view.hideTasksLoading()
                }
                .subscribe({t ->
                    Log.d("getTasks","next")
                    view.updateTasks(t)
                },{t ->
                    Log.d("getTasks",t.message)
                    view.hideTasksLoading()
                }))
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onGroupItemClick(groupName: String, groupId: String) {
        Log.d("groupIdhehe",groupId)
        repo.prefs.setCurrentGroupId(groupId)
        repo.prefs.setCurrentGroupName(groupName)
    }
}