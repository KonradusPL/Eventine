package com.racjonalnytraktor.findme3

import android.content.Context
import com.racjonalnytraktor.findme3.data.model.User
import com.racjonalnytraktor.findme3.data.repository.BaseRepository
import io.realm.Realm
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    @Mock
    private lateinit var mMockContext: Context

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun realmTest(){
        Realm.init(mMockContext)

        val repo = BaseRepository()

        repo.preferences.createUser(User("fb","michnophoto.php","Marcin Michno","3","1","@"))
        assert(repo.preferences.getUserEmail() == "@")
    }
}
