package com.racjonalnytraktor.findme3.utils

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.Model1
import com.racjonalnytraktor.findme3.data.network.model.Help
import com.racjonalnytraktor.findme3.data.network.model.UserSimple
import com.racjonalnytraktor.findme3.data.repository.ApplicationRepository
import com.racjonalnytraktor.findme3.ui.adapters.manage.Job
import com.racjonalnytraktor.findme3.ui.adapters.manage.Worker
import java.util.*

object ClassTransform {

    val appRepo = ApplicationRepository

    fun fromPeopleArrayToJobs(people: ArrayList<UserSimple>): ArrayList<Job>{
        Log.d("fromPeopleArrayToJobs:",people.toString())
        val jobs = ArrayList<Job>()
        val jobNames = HashMap<String,Int>()
        for(person in people){
            val i = jobNames[person.subgroup]
            if (i != null){
                jobs[i].workers.add(Worker(person.name,person.id))
                jobs[i].workersCount++
            }else{
                jobs.add(Job(person.subgroup,1, arrayListOf(Worker(person.name,person.id))))
                jobNames[person.subgroup] = jobs.lastIndex
            }
        }
        Log.d("fromPeopleArrayToJobs:",jobs.toString())
        return jobs
    }

    fun fromActionToModelH(action: Action): Model1{
        val dateString = StringHelper.getTimeForAction(Date(),action.createdAt)
        return Model1(action.title,action.desc,dateString,action.id)
    }

    fun fromHelpToModelH(help: Help): Model1{
        val members = appRepo.getMembers()

        val stringBuilder = StringBuilder("Zaakceptowali:")
        for(a in help.accepted){
            for(member in members){
                if(member.id == a)
                    stringBuilder.append(" ${member.name},")
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
        return Model1(help.caller.fullName,stringBuilder.toString(),"")
    }
}