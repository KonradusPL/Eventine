package com.racjonalnytraktor.findme3.utils

import android.app.Activity
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Observable

class PermissionsHelper(val context: Activity) {
    enum class PermissionState{
        GRANTED,
        RATIONALE_SHOULD_BE_SHOWN,
        DENIED
    }

    fun checkPermission(permission: String): Observable<PermissionState> {

        return Observable.create {
            emitter ->
            val permissionListener = object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Log.d("permission","GRANTED")
                    emitter.onNext(PermissionState.GRANTED)
                    emitter.onComplete()
                }
                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    Log.d("permission","RATIONALE_SHOULD_BE_SHOWN")
                    emitter.onNext(PermissionState.RATIONALE_SHOULD_BE_SHOWN)
                    emitter.onComplete()
                }
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Log.d("permission","DENIED")
                    emitter.onNext(PermissionState.DENIED)
                    emitter.onComplete()
                }

            }
            Dexter.withActivity(context)
                    .withPermission(permission)
                    .withListener(permissionListener)
                    .check()

        }
    }
}