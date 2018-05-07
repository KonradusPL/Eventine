package com.racjonalnytraktor.findme3.data.repository.create

import com.racjonalnytraktor.findme3.data.model.Friend
import io.reactivex.Flowable
import io.reactivex.Observable

class CreateRepositoryImpl: CreateRepository {
    override fun getFriends(): Observable<List<Friend>> {
        return Observable.just(arrayListOf(
                Friend("Andrzej Duda","http://i.iplsc.com/andrzej-duda-podczas-spotkania-z-donaldem-tuskiem/0004ZMLPCT2T0HH0-C116-F4.jpg"),
                Friend("Patryk Godek","https://scontent-waw1-1.xx.fbcdn.net/v/t1.0-9/23915570_1584813361576152_8623452931475651029_n.jpg?_nc_cat=0&oh=c9a687eae15221b64e56a0128e563af2&oe=5B8CD0DF"),
                Friend("Zbigniew Stonoga","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1cz45MivnsmZMclpQBOq_NkBLxAwtqXobqhYCo7gTZzcyx0_8aw-BnZs")))
    }
}