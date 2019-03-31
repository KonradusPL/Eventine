package com.racjonalnytraktor.findme3.ui.adapters.manage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

class Worker() : Parcelable {

    lateinit var name: String
    lateinit var id: String
    var selected = false
    lateinit var location: String
    lateinit var profileUrl: String

    @SuppressLint("ParcelClassLoader")
    constructor(parcel: Parcel) : this() {
        val bundle = parcel.readBundle()
        name = bundle.getString("name")
        id = bundle.getString("id")
        profileUrl = bundle.getString("profileUrl")
        location = bundle.getString("location")
        selected = bundle.getBoolean("selected",false)
    }

    constructor(name: String, id: String,location: String, selected: Boolean = false, profileUrl: String = "") : this() {
        this.name = name
        this.id = id
        this.selected = selected
        this.profileUrl = profileUrl
        this.location = location
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        val bundle = Bundle()
        bundle.putString("name",name)
        bundle.putString("id",id)
        bundle.putString("profileUrl",profileUrl)
        bundle.putBoolean("selected",selected)
        bundle.putString("location",location)
        parcel.writeBundle(bundle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Worker> {
        override fun createFromParcel(parcel: Parcel): Worker {
            return Worker(parcel)
        }

        override fun newArray(size: Int): Array<Worker?> {
            return arrayOfNulls(size)
        }
    }
}