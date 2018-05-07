package com.racjonalnytraktor.findme3.data.repository.join

import com.racjonalnytraktor.findme3.data.model.Invitation
import io.reactivex.Observable

class JoinRepositoryImpl: JoinRepository {
    override fun getInvitations(): Observable<List<Invitation>> {
        return Observable.just(arrayListOf(
                Invitation("Akademia milionerów","Patryk Godek","https://i.ytimg.com/vi/mzhXCC1cHGo/hqdefault_live.jpg"),
                Invitation("Derby","Resovia","http://polskielogo.net/wp-content/uploads/2015/11/67-resovia-ikona.jpg"),
                Invitation("Słodkie kotki","Jakub Mularski","https://static9.depositphotos.com/1308396/1121/i/950/depositphotos_11211786-stock-photo-kitten-face.jpg"),
                Invitation("Fani piesełów","Jakun Mularski","https://vignette.wikia.nocookie.net/bayonetta/images/0/05/Doge.png/revision/latest?cb=20140807211510")))
    }

}