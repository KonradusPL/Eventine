package com.racjonalnytraktor.findme3.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.racjonalnytraktor.findme3.R
import com.racjonalnytraktor.findme3.data.local.SharedPrefs
import com.racjonalnytraktor.findme3.utils.DeviceInfo
import com.racjonalnytraktor.findme3.utils.PermissionsHelper
import es.dmoral.toasty.Toasty
import io.reactivex.Completable
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_map.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


open class BaseActivity : AppCompatActivity(),MvpView{

    private val REQUEST_CHECK_SETTINGS = 1905

    protected lateinit var progressBar: ProgressBar
    private lateinit var mDeviceInfo: DeviceInfo
    private lateinit var mPermissionsHelper: PermissionsHelper
    lateinit var sharedPrefs: SharedPrefs

    private var mPermissionStatus = false

    var isLive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDeviceInfo = DeviceInfo(this)
        mPermissionsHelper = PermissionsHelper(this)
        sharedPrefs = SharedPrefs(this)
    }

    override fun onStart() {
        super.onStart()
        isLive = true
    }

    override fun onStop() {
        super.onStop()
        isLive = false
    }

    override fun hideKeyboard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPrefs(): SharedPrefs {
        return sharedPrefs
    }

    override fun showMessage(message: Int, type: MvpView.MessageType) {
        var newMessage = ""
        try {
            newMessage = resources.getString(message)
        }catch (e: Exception){
            Log.d("exception",e.toString())
        }
        showMessage(newMessage,type)

    }

    override fun showMessage(message: String, type: MvpView.MessageType) {
        val toast = when(type){
            MvpView.MessageType.NORMAL -> Toasty.normal(this, message)
            MvpView.MessageType.ERROR -> Toasty.error(this, message)
            MvpView.MessageType.INFO -> Toasty.info(this,message)
            MvpView.MessageType.SUCCESS -> Toasty.success(this, message)
        }
        toast.setGravity(Gravity.TOP.xor(Gravity.CENTER_HORIZONTAL),0,64)
        toast.show()
    }

    override fun isConnectedToNetwork(): Boolean {
        return mDeviceInfo.isConnected()
    }

    override fun checkPermission(permission: String): Observable<PermissionsHelper.PermissionState> {
        return mPermissionsHelper.checkPermission(permission)
    }

    override fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun increaseAlpha() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun decreaseAlpha() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkLocationSettings(): Completable {

       return Completable.create {emitter ->
            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(LocationRequest().apply {
                        interval = 1000
                        fastestInterval = 1000
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    })


            val settingsClient = LocationServices.getSettingsClient(this)
            val taskCheckSettings = settingsClient.checkLocationSettings(builder.build())


            taskCheckSettings.addOnSuccessListener{
                Log.d("Michno", "OnSuccessListener")
                emitter.onComplete()
            }

            taskCheckSettings.addOnFailureListener { exception ->
                if (exception is ResolvableApiException){
                    Log.d("Michno","OnFailureListener")
                    try {
                        exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                        doAsync {
                            while (!mPermissionStatus){
                                Thread.sleep(100)
                            }
                            uiThread {
                                emitter.onComplete()
                            }
                        }
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHECK_SETTINGS){
            mPermissionStatus = true
        }
    }

    override fun isAttached(): Boolean {
        return isLive
    }

    override fun getCtx(): Context {
        return this
    }

}