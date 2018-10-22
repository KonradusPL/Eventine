package com.racjonalnytraktor.findme3.data.local.db

import android.util.Log
import com.racjonalnytraktor.findme3.data.local.model.UserRealm
import com.racjonalnytraktor.findme3.data.model.User
import io.realm.Realm

class RealmLocalDb: LocalDb {

    private val mRealm = Realm.getDefaultInstance()

    override fun getUserToken(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.token
    }

    override fun setUserToken(token: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.token = token
        }
    }

    override fun setUserFullName(fullName: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.fullName = fullName
        }
    }

    override fun getUserFullName(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.fullName
    }

    override fun setUserEmail(email: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.email = email
        }
    }

    override fun getUserEmail(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.email    }

    override fun isUserLoggedIn(): Boolean {
        val user = mRealm.where(UserRealm::class.java).findFirst()

        return user?.isUserLoggedIn ?: false
    }

    override fun setIsUserLoggedIn(value: Boolean) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.isUserLoggedIn = value
        }
    }

    override fun isPartner(): Boolean {
        val user = mRealm.where(UserRealm::class.java).findFirst()

        return user?.isPartner ?: false
    }

    override fun setIsPartner(value: Boolean) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.isPartner = value
        }
    }

    override fun setUserProfileImage(value: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.profileImage = value
        }
    }

    override fun getUserProfileImage(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.profileImage    }

    override fun getCurrentUser(): User {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        Log.d("getCurrentUser","popo ${user.email}")
        return user.getUser()
    }

    override fun setCurrentUser(value: User) {
        /*val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.name = value.name
            user.email = value.email
            user.profileImage = value.profileUri
            user.facebookId = value.facebookId
            user.token = value.token
        }*/
        createUser(value)
    }

    override fun setFacebookId(value: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.facebookId = value
        }
    }

    override fun getFacebookId(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.facebookId    }

    override fun setCurrentGroupId(value: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.eventId = value
        }
    }

    override fun getCurrentGroupId(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.eventId    }

    override fun setCurrentGroupName(value: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.eventName = value
        }
    }

    override fun getCurrentGroupName(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.eventName
    }

    override fun setRole(value: String) {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        mRealm.executeTransaction {
            user.role = value
        }
    }

    override fun getRole(): String {
        val user = mRealm.where(UserRealm::class.java).findFirst() ?: UserRealm()
        return user.role    }

    override fun createUser(user: User) {
        mRealm.executeTransaction {
            val userRealm = mRealm.where(UserRealm::class.java).findFirst()
            if(userRealm == null){
                Log.d("useruser","asdsad")
                val newUserRealm = mRealm.createObject(UserRealm::class.java)
                newUserRealm?.setUser(user)
            }else
                userRealm.setUser(user)
        }
    }

    override fun removeUser() {
        mRealm.executeTransaction {
            mRealm.where(UserRealm::class.java).findAll().deleteAllFromRealm()
        }
    }
}