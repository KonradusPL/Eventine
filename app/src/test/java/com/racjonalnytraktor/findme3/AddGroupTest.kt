package com.racjonalnytraktor.findme3

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.ActionsResponse
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.MembersResponse
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.model.ChangeSubGroupRequest
import com.racjonalnytraktor.findme3.data.network.model.CreateGroupRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import org.junit.Test
import kotlin.collections.HashMap

class AddGroupTest {

    val token1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6IktvbnJhZCBQxJlrYWxhIiwiaWQiOiI1YmM0ZjcxMWI2ZTBlYzAwMTBmYTNlMDIiLCJpYXQiOjE1Mzk2OTg2MTYsImV4cCI6MTU0MDMwMzQxNn0.-NvuTDH6kqwpf9_KThbnYdvPBiquuSDS7W4OGGz10U0"
    val token2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6Ik1hcmNpbiBLb3dhbHNraSIsImlkIjoiNWJhYzkyYjUwYzUxZjMwMDEwZjVkMDhmIiwiaWF0IjoxNTM4MDM2NDA1LCJleHAiOjE1Mzg2NDEyMDV9.i4_JXB9iREQlJ7ioPWvf4algZSaJLxzpj6PZOJygf7Y"
    val token3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6IlRhZGV1c3ogS293YWxza2kiLCJpZCI6IjViYWM5MmU5MGM1MWYzMDAxMGY1ZDA5MCIsImlhdCI6MTUzODAzNjQ1NywiZXhwIjoxNTM4NjQxMjU3fQ.4lvyJWCB0q4o7cG0Oeh770XSeu4RenO-KMWRi9NojG4"

    val id1 = "5bac92b50c51f30010f5d08f"
    val id2 = "5bac92e90c51f30010f5d090"
    val fbId = "1076056665880560"
    val request1 = RegisterRequest("test1@test.pl", "Jan Kowalski", "password1")
    val request2 = RegisterRequest("test2@test.pl", "Marcin Kowalski", "password2")
    val request3 = RegisterRequest("test3@test.pl", "Tadeusz Kowalski", "password3")
    val request4 = RegisterRequest("test4@test.pl", "Jakub Mularski", "password4")
    val request5 = RegisterRequest("test5@test.pl", "Marcin Michno", "password5")
    val request6 = RegisterRequest("test6@test.pl", "Adam Nowak", "password6")

    val grupaTestowa1 = "5bc4f824b6e0ec0010fa3e03"

    val rest = RetrofitRest().networkService

    @Test
    fun changeSubGroup() {
        val request = ChangeSubGroupRequest("5bc5bfa446e62a00100cc95a",grupaTestowa1,"admin")
        println(request)
        rest.changeSubGroups(token1, request)
                .subscribe({ t: String? ->
                    println("Wynik: $t")
                }, { t: Throwable? ->
                    println("Wynik: ${t?.message.orEmpty()}")
                })
    }

    @Test
    fun addGroup() {
        val request = CreateGroupRequest("Grupa domyślna3", emptyList(),emptyList())
        println(request)
        rest.createGroup(token1, request)
                .subscribe({ t: String? ->
                    println("Wynik: $t")
                }, { t: Throwable? ->
                    println("Wynik: ${t?.message.orEmpty()}")
                })
    }

    @Test
    fun registerTestingUsers() {
        println(request6)
        rest.register(request6)
                .subscribe({ t: RegisterResponse? ->
                    println(t!!.token)
                    assert(true)
                }, { t: Throwable? ->
                    println(t.toString())
                    assert(false)
                })
    }

    @Test
    fun loginTestingUsers() {
        val request = LoginRequest(request6.email,request6.password)
        rest.login(request)
                .subscribe({ t: LoginResponse? ->
                    println(t!!.token)
                    assert(true)
                }, { t: Throwable? ->
                    assert(false)
                })
    }

    @Test
    fun createAction(){
        val action = CreateActionRequest()
        action.apply {
            type = "ping"
            title = "Title3"
            desc = "Descr3"
            plannedTime = ""
            geo = arrayListOf(50.747765,19.178419)
            groupId = "5bb206e3c4b7060010e4c667"
            people = arrayListOf(id1,id2)
        }
        rest.createAction(token1,action)
                .subscribe({ t: String? ->
                    println(t)
                },{t: Throwable? ->
                    print(t)
                })

    }

    @Test
    fun getGroupUsers(){
        println("token: $token1 , groupId: $grupaTestowa1")
        rest.getGroupMembers(token1,grupaTestowa1)
                .subscribe({t: MembersResponse? ->
                    println("users:  ${t?.people.toString()}")
                },{t: Throwable? ->
                    println(t.toString())
                })
    }
    @Test
    fun getPings() {
        println("token: $token1, groupId: $grupaTestowa1")
        rest.getActions(token1, grupaTestowa1,"ping")
                .map { t: ActionsResponse -> t.actions }
                .toObservable()
                .flatMapIterable { t -> t }
                .subscribe({ t: Action? ->
                    println("action:  ${t?.toString()}")
                }, { t: Throwable? ->
                    println(t.toString())
                })
    }
    @Test
    fun getPingsTest() {
        println("token: $token1, groupId: $grupaTestowa1")
        rest.getActionsTest(token1, grupaTestowa1,"ping")
                .subscribe({ t: HashMap<String,Any>? ->
                    println("actions:  ${t?.toString()}")
                }, { t: Throwable? ->
                    println(t.toString())
                })
    }

    @Test
    fun updateLocationTest(){
        val map = HashMap<String,Any>()
        map["groupId"] = grupaTestowa1
        map["locationTag"] = "Pokoik"
        rest.updateLocation(token1,map).subscribe({
            t: String ->
            println(t)
        },{t: Throwable? ->
            println(t.toString())
        })
    }

}