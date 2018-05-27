package com.racjonalnytraktor.findme3.data.network.facebook

import android.util.Log
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.Callable
import android.os.Bundle



class FacebookNetwork {

    fun getUserBasicInfo(): Single<GraphResponse>{
        val accessToken = AccessToken.getCurrentAccessToken()
        val single = Single.create<GraphResponse> { emitter ->

            val parameters = Bundle()
            parameters.putString("fields", "id,name,picture")

            val request = GraphRequest.newGraphPathRequest(accessToken,
                    accessToken.userId, { response: GraphResponse? ->
                Log.d("graphresponse",response.toString())
                if(response == null)
                    emitter.onError(Throwable("null"))
                else
                    emitter.onSuccess(response)
            })
            request.parameters = parameters
            request.executeAndWait()
        }

        return single
    }

    fun getAccessToken(): AccessToken{
        return AccessToken.getCurrentAccessToken()
    }
}