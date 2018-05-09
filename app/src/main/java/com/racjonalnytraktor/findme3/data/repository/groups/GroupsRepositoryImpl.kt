package com.racjonalnytraktor.findme3.data.repository.groups

import com.racjonalnytraktor.findme3.data.model.Group
import com.racjonalnytraktor.findme3.data.model.Task
import io.reactivex.Observable

class GroupsRepositoryImpl: GroupsRepository {
    override fun getTasks(): Observable<List<Task>> {
        return Observable.just(arrayListOf(Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204"),
                Task("Follow the damn train","Big smoke","https://vignette.wikia.nocookie.net/gtawiki/images/b/bd/BigSmoke-GTASA.jpg/revision/latest?cb=20100105192204")))
    }

    override fun getGroups(): Observable<List<Group>> {
        return Observable.just(arrayListOf(Group("Cebulaki","https://sklep-nasiona.pl/images/detailed/31/cebula-topolska-nasiona-3.jpg"),
                Group("Kotki","http://blog.zooplus.pl/wp-content/uploads/sites/8/2014/03/maly_kotek.jpg"),
                Group("Pieski","https://i.imgflip.com/1e5b67.jpg"),
                Group("Resoviacy","https://s2.fbcdn.pl/3/clubs/27733/logos/s/herb-rywala-resovia2000_69.jpg")))
    }
}