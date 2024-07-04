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

    override suspend fun changeAppMode(newmode: AppMode) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_MODE] = newmode.name
        }
    }

    override fun readAppMode(): Flow<AppMode> {
        return context.dataStore.data.map { preferences ->
            val mode = preferences[PreferenceKeys.APP_MODE]
            if (mode == AppMode.WHATSAPP_BUSINESS.name) AppMode.WHATSAPP_BUSINESS else AppMode.WHATSAPP
        }
    }

    override fun readWhatsappPermission(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.WHATSAPP_PERMISSION] ?: false
        }
    }

    override fun readWhatsappBusinessPermission(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.WHATSAPP_BUSINESS_PERMISSION] ?: false
        }
    }

    override suspend fun whatsApppermissionGranted() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.WHATSAPP_PERMISSION] = true
        }
    }

    override suspend fun whatsAppBusinesspermissionGranted() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.WHATSAPP_BUSINESS_PERMISSION] = true
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = "USER_SETTINGS")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val APP_ENTRY = booleanPreferencesKey("APP_ENTRY")
    val APP_MODE = stringPreferencesKey("APP_MODE")
    val WHATSAPP_PERMISSION = booleanPreferencesKey("WHATSAPP_PERMISSION")
    val WHATSAPP_BUSINESS_PERMISSION = booleanPreferencesKey("WHATSAPP_BUSINESS_PERMISSION")
}