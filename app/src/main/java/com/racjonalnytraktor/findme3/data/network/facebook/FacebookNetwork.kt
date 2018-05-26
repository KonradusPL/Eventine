package com.racjonalnytraktor.findme3.data.network.facebook

import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.Callable

class FacebookNetwork {

    fun getUserBasicInfo(): Single<GraphResponse>{
        val accessToken = AccessToken.getCurrentAccessToken()
        val single = Single.create<GraphResponse> { emitter ->
            val response = GraphRequest.newGraphPathRequest(accessToken,
                    accessToken.userId, { response: GraphResponse? ->
                Log.d("asdasd",response.toString())
                if(response == null)
                    emitter.onError(Throwable("null"))
                else
                    emitter.onSuccess(response)
            }).executeAndWait()
        }

        return single
    }

    fun getAccessToken(): AccessToken{
        return AccessToken.getCurrentAccessToken()
    }
}