package id.afif.binarchallenge6.Helper

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {



    suspend fun setData(username : String,password : String) {
        context.dataStore.edit { pref ->
            pref[KEY_USERNAME] = username
            pref[KEY_PASSWORD] = password
            pref[KEY_LOGIN] = true
        }
    }

    fun getUsername() : Flow<String>{
        return context.dataStore.data.map { pref ->
            pref[KEY_USERNAME] ?: "_"
        }
    }

    fun getUserLogin() : Flow<Array<String>>{
        return context.dataStore.data.map { pref ->
            arrayOf(pref[KEY_USERNAME] ?: "",pref[KEY_PASSWORD]?:"")
        }
    }

    fun isLoggin() : Flow<Boolean>{
        return context.dataStore.data.map { pref ->
            pref[KEY_LOGIN] ?: false
        }
    }

    suspend fun logoutSession(){
        context.dataStore.edit { pref ->
            pref.remove(KEY_PASSWORD)
            pref.remove(KEY_USERNAME)
            pref.remove(KEY_LOGIN)
        }
    }



    companion object{
        private const val DATA_STORE_NAME = "dataStore123"


        private val KEY_USERNAME = stringPreferencesKey("KEy_username")
        private val KEY_PASSWORD = stringPreferencesKey("Key_password")
        private val KEY_LOGIN = booleanPreferencesKey("Key_bool")
        private val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)

    }

}