package com.racjonalnytraktor.findme3.data.network

import com.racjonalnytraktor.findme3.data.model.UpdateTokenRequest
import com.racjonalnytraktor.findme3.data.model.Action
import com.racjonalnytraktor.findme3.data.model.ActionsResponse
import com.racjonalnytraktor.findme3.data.model.new.CreateActionRequest
import com.racjonalnytraktor.findme3.data.network.model.*
import com.racjonalnytraktor.findme3.data.network.model.changegroups.SubGroupPeople
import com.racjonalnytraktor.findme3.data.network.model.createping.Ping
import com.racjonalnytraktor.findme3.data.network.model.info.Info
import com.racjonalnytraktor.findme3.data.network.model.login.LoginRequest
import com.racjonalnytraktor.findme3.data.network.model.login.LoginResponse
import com.racjonalnytraktor.findme3.data.network.model.pings.PingsResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterFbResponse
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterRequest
import com.racjonalnytraktor.findme3.data.network.model.register.RegisterResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface Routes {

    //auth////
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Observable<LoginResponse>

    @POST("auth/social")
    fun registerByFacebook(@Body registerRequest: RegisterFbRequest): Single<RegisterFbResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Single<RegisterResponse>

    //groups////

    @POST("group/create")
    fun createGroup(@Header("X-Token") token: String, @Body request: CreateGroupRequest): Single<String>

    @POST("group/join")
    fun joinGroup(@Header("X-Token") token: String, @Body request: JoinRequest): Single<String>

    @POST("group/acceptInvitation")
    fun acceptInvitation(@Header("X-Token") token: String, @Body request: AcceptInvitationRequest)
            : Single<String>
    @GET("group/subgroups/{groupId}")
    fun getSubGroups(@Header("X-Token") token: String)

    @FormUrlEncoded
    @POST("group/location")
    fun updateLocation(@Header("X-Token") token: String, @FieldMap data: HashMap<String,Any>)
    :Single<String>

    @GET("group/allSubGroups/{groupId}")
    fun getAllSubGroups(@Header("X-Token") token: String,@Path("groupId") groupId: String):
            Observable<List<String>>

    @POST("group/changeSubGroup")
    fun changeSubGroups(@Header("X-Token")token: String, @Body request: ChangeSubGroupRequest): Single<String>

    @GET("group/members/{groupId}")
    fun getGroupMembers(@Header("X-Token")token: String,@Path("groupId") groupId: String)
    :Single<MembersResponse>

    //actions////

    @POST("actions/create")
    fun createAction(@Header("X-Token")token: String, @Body action: CreateActionRequest): Single<String>

    @POST("ping/create")
    fun createPing(@Header("X-Token")token: String, @Body request: Ping): Single<String>

    @GET("actions/list/{groupId}/{type}")
    fun getActions(@Header("X-Token")token: String, @Path("groupId")groupId: String, @Path("type")type: String)
            : Single<ActionsResponse>

    @GET("actions/list/{groupId}/{type}")
    fun getActionsTest(@Header("X-Token")token: String, @Path("groupId")groupId: String, @Path("type")type: String)
            : Single<HashMap<String,Any>>

    @GET("info/list/{groupId}")
    fun getInfos(@Header("X-Token")token: String, @Path("groupId") groupId: String)
            : Observable<HistoryInfosResponse>


    @POST("info/create")
    fun createInfo(@Header("X-Token")token: String, @Body request: Info): Single<String>

    @POST("actions/end")
    fun endPing(@Header("X-Token")token: String, @Body request: EndPing): Single<String>

    @POST("actions/inProgress")
    fun setPingToInProgress(@Header("X-Token")token: String, @Body request: EndPing): Single<String>

    //////

    @POST("notif/updateToken")
    fun updateNotifToken(@Header("X-Token")token: String, @Body notifToken: UpdateTokenRequest): Single<String>

    @GET("group/subgroups/{groupId}")
    fun getPeopleInSubGroups(@Header("X-Token")token: String, @Path("groupId") groupId: String)
            :Observable<SubGroupPeople>

    //user////

    @GET("user/invitations")
    fun getInvitations(@Header("X-Token")token: String): Observable<InvitationResponse>

    @GET("user/friends")
    fun getFriends(@Header("X-Token")token: String): Observable<FriendsResponse>

    @GET("user/tasks")
    fun getTasks(@Header("X-Token")token: String): Observable<PingsResponse>

    @GET("user/groupList")
    fun getGroups(@Header("X-Token")token: String): Observable<GroupsResponse>
}