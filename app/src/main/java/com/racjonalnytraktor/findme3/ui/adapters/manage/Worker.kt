package com.racjonalnytraktor.findme3.ui.adapters.manage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable

class Worker() : Parcelable {

    lateinit var name: String

    @SuppressLint("ParcelClassLoader")
    constructor(parcel: Parcel) : this() {
        val bundle = parcel.readBundle()
        name = bundle.getString("name")
    }

    constructor( name: String) : this() {
        this.name = name

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        val bundle = Bundle()
        bundle.putString("name",name)
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