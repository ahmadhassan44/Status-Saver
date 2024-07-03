package com.loc.newsapp.data.manger

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.livesaver.app.domain.AppMode
import com.example.livesaver.app.domain.LocalUserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalUserMangerImpl(
    private val context: Context
) : LocalUserManager {

    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }
    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_ENTRY] ?: false
        }
    }

    override suspend fun chnageAppMode() {
        context.dataStore.edit { settings ->
            val currentMode = context.dataStore.data.map { preferences ->
                preferences[PreferenceKeys.APP_MODE] ?: AppMode.WHATSAPP.name
            }.first()

            val newMode = if (currentMode == AppMode.WHATSAPP.name) {
                AppMode.WHATSAPP_BUSINESS.name
            } else {
                AppMode.WHATSAPP.name
            }

            context.dataStore.edit { settings ->
                settings[PreferenceKeys.APP_MODE] = newMode
            }
        }
    }

    override fun readAppMode(): Flow<AppMode> {
        return context.dataStore.data.map { preferences ->
            val mode = preferences[PreferenceKeys.APP_MODE]
            if (mode == AppMode.WHATSAPP_BUSINESS.name) AppMode.WHATSAPP_BUSINESS else AppMode.WHATSAPP
        }
    }

}

private val readOnlyProperty = preferencesDataStore(name = "USER_SETTINGS")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey("APP_ENTRY")
    val APP_MODE= stringPreferencesKey("APP_MODE")
}