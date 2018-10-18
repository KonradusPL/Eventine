package com.racjonalnytraktor.findme3.ui.map.fragments

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.Help
import com.racjonalnytraktor.findme3.data.network.model.UserSimple
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.data.repository.HistoryRepository
import com.racjonalnytraktor.findme3.ui.adapters.HistoryAdapter
import com.racjonalnytraktor.findme3.ui.base.BasePresenter
import com.racjonalnytraktor.findme3.utils.ClassTransform

class HistoryPresenter<V: HistoryMvp.View>: BasePresenter<V>(),HistoryMvp.Presenter<V>
,HistoryAdapter.ClickListener{

    val repo = HistoryRepository
    val mMembers = ArrayList<UserSimple>()

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        onActionsButtonClick()
    }

    override fun onActionsButtonClick() {
        compositeDisposable.clear()
        view.clearList("info")
        view.showProgress()
        compositeDisposable.add(repo.getActions()
                .subscribe({actions: List<Action>? ->
                    view.hideProgress()
                    if(actions != null){
                        repo.actions.clear()
                        for(action in actions){
                            val model1 = ClassTransform.fromActionToModelH(action)
                            repo.actions.add(model1)
                        }
                        view.updateActions(repo.actions,"ping")
                    }
                },{t: Throwable? ->
                    if(repo.actions.isNotEmpty())
                        view.updateActions(repo.actions,"ping")
                    view.hideProgress()
                    Log.d("error",t.toString())
                }))
    }

    override fun onHelpButtonClick() {
        compositeDisposable.clear()
        view.clearList("actions")
        view.showProgress()
        compositeDisposable.add(repo.getHelps()
                .subscribe({helps: ArrayList<Help>? ->
                    view.hideProgress()
                    if (helps != null){
                        Log.d("onHelpButtonClick",helps.toString())
                        repo.listHelp.clear()
                        for(help in helps){
                            val model1 = ClassTransform.fromHelpToModelH(help)
                            repo.listHelp.add(model1)
                        }
                        view.updateActions(repo.listHelp,"help")
                    }
                },{t: Throwable? ->
                    if(repo.actions.isNotEmpty())
                        view.updateActions(repo.listHelp,"help")
                    view.hideProgress()
                    Log.d("error",t.toString())
                }))
    }

    override fun onInfoClick(info: Info) {
       // view.showEndPingBar(info)
    }

    override fun onPingClick(ping: Ping) {
        view.showEndPingBar(ping)
    }

    override fun onAllButtonClick() {
    }

    override fun onDetach() {
        super.onDetach()
        compositeDisposable.clear()
    }

}