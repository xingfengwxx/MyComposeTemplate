package com.wangxingxing.mycomposeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.wangxingxing.mycomposeapp.model.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val USER_DATASTORE_NAME = "user_prefs"

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_DATASTORE_NAME
)

@Singleton
class UserLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
){
    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val TOKEN = stringPreferencesKey("token")
    }

    suspend fun saveUserInfo(userInfo: UserInfo) {
        context.userDataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userInfo.id
            prefs[Keys.USERNAME] = userInfo.username
            prefs[Keys.EMAIL] = userInfo.email
            prefs[Keys.TOKEN] = userInfo.token
        }
    }

    suspend fun clearUserInfo() {
        context.userDataStore.edit { it.clear() }
    }

    fun getUserInfo(): Flow<UserInfo?> {
        return context.userDataStore.data.map { prefs ->
            val id = prefs[Keys.USER_ID] ?: return@map null
            val username = prefs[Keys.USERNAME] ?: return@map null
            val email = prefs[Keys.EMAIL] ?: ""
            val token = prefs[Keys.TOKEN] ?: ""
            UserInfo(id = id, username = username, email = email, token = token)
        }
    }

    fun getUserForUi(): Flow<UserInfo?> = getUserInfo()
}


