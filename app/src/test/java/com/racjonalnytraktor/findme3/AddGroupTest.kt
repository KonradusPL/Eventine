package com.racjonalnytraktor.findme3

import android.util.Log
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.MembersResponse
import com.racjonalnytraktor.findme3.data.network.RetrofitRest
import com.racjonalnytraktor.findme3.data.network.model.CreateGroupRequest
import com.racjonalnytraktor.findme3.data.network.model.GroupsResponse
import com.racjonalnytraktor.findme3.data.network.model.UserSimple
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import org.junit.Test
import java.util.*

class AddGroupTest {

    val token1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6IkphbiBLb3dhbHNraSIsImlkIjoiNWJhYzkyN2QwYzUxZjMwMDEwZjVkMDhlIiwiaWF0IjoxNTM4MDM2MzQ5LCJleHAiOjE1Mzg2NDExNDl9.4K1c_fN50r8qgTpUanUw_p15oAJ9924lzChNzQ13U9o"
    val token2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6Ik1hcmNpbiBLb3dhbHNraSIsImlkIjoiNWJhYzkyYjUwYzUxZjMwMDEwZjVkMDhmIiwiaWF0IjoxNTM4MDM2NDA1LCJleHAiOjE1Mzg2NDEyMDV9.i4_JXB9iREQlJ7ioPWvf4algZSaJLxzpj6PZOJygf7Y"
    val token3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmdWxsTmFtZSI6IlRhZGV1c3ogS293YWxza2kiLCJpZCI6IjViYWM5MmU5MGM1MWYzMDAxMGY1ZDA5MCIsImlhdCI6MTUzODAzNjQ1NywiZXhwIjoxNTM4NjQxMjU3fQ.4lvyJWCB0q4o7cG0Oeh770XSeu4RenO-KMWRi9NojG4"

    val id1 = "5bac92b50c51f30010f5d08f"
    val id2 = "5bac92e90c51f30010f5d090"

    val request1 = RegisterRequest("test1@test.pl", "Jan Kowalski", "password1")
    val request2 = RegisterRequest("test2@test.pl", "Marcin Kowalski", "password2")
    val request3 = RegisterRequest("test3@test.pl", "Tadeusz Kowalski", "password3")
    val request4 = RegisterRequest("test4@test.pl", "Jakub Mularski", "password4")
    val request5 = RegisterRequest("test5@test.pl", "Marcin Michno", "password5")

    val grupaTestowa1 = "5bb206e3c4b7060010e4c667"

    val rest = RetrofitRest().networkService

    @Test
    fun addGroup() {
        val request = CreateGroupRequest("Grupa testowa1", emptyList(), arrayListOf(id1, id2))
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
        print(request5)
        rest.register(request5)
                .subscribe({ t: RegisterResponse? ->
                    print(t!!.token)
                    assert(true)
                }, { t: Throwable? ->
                    assert(false)
                })
    }

    @Test
    fun loginTestingUsers() {
        val request = LoginRequest(request1.email,request1.password)
        rest.login(request)
                .subscribe({ t: LoginResponse? ->
                    print(t!!.token)
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
            title = "Title"
            descr = "Descr"
            plannedTime = Date()
            geo = arrayListOf(50.747765,19.178419)
            groupId = "Id5baf837cc4b7060010e4c663"
            users.add("5bac92e90c51f30010f5d090")
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
        println("token: $token1, groupId: $grupaTestowa1")
        rest.getGroupMembers(token1,grupaTestowa1)
                .subscribe({t: MembersResponse? ->
                    println("users:  ${t?.people.toString()}")
                },{t: Throwable? ->
                    println(t.toString())
                })
    }
}