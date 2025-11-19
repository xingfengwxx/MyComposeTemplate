package com.wangxingxing.mycomposeapp.di // 推荐放在 di 包下

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_prefs"

/**
 * author : 王星星
 * date : 2025/11/19 10:49
 * email : 1099420259@qq.com
 * description : 
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            // 如果 DataStore 文件损坏，用一个空本来替换它
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            // 在 IO 线程上创建和更新数据
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            // 创建文件的具体路径
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }
}
