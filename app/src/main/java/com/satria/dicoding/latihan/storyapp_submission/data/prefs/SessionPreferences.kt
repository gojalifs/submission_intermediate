package com.satria.dicoding.latihan.storyapp_submission.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session")

class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val sessionToken = stringPreferencesKey("token")

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[sessionToken] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map {
            it[sessionToken]
        }
    }

    suspend fun deleteToken(): Boolean {
        dataStore.edit {
            it.remove(sessionToken)
        }
        return true
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}