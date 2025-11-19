package com.wangxingxing.mycomposeapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// 通过委托创建 DataStore 实例
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class UserDataRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
    }

    /**
     * 提供一个 Flow，用于观察存储的凭据。
     * 如果用户名或密码不存在，则返回 null。
     */
    val savedCredentials: Flow<Pair<String, String>?> = context.dataStore.data
        .map { preferences ->
            val username = preferences[PreferencesKeys.USERNAME]
            val password = preferences[PreferencesKeys.PASSWORD]
            if (username != null && password != null) {
                Pair(username, password)
            } else {
                null
            }
        }

    /**
     * 保存用户名和密码。
     */
    suspend fun saveCredentials(username: String, password: String) {
        context.dataStore.edit {
            it[PreferencesKeys.USERNAME] = username
            it[PreferencesKeys.PASSWORD] = password
        }
    }

    /**
     * 清除存储的凭据，用于登出。
     */
    suspend fun clearCredentials() {
        context.dataStore.edit {
            it.clear()
        }
    }
}
