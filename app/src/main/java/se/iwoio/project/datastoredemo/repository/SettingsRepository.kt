package se.iwoio.project.datastoredemo.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val settingsDataStore: DataStore<Preferences>
) {
    private companion object {
        val KEY_ORDERBYTITLE = booleanPreferencesKey(
            name = "orderByTitle"
        )
        val KEY_ORDERBYDESC = booleanPreferencesKey(
            name = "orderByDesc"
        )
    }

    suspend fun setOrderByTitle(
        orderByTitle: Boolean
    ) {
        settingsDataStore.edit { preferences ->
            preferences[KEY_ORDERBYTITLE] = orderByTitle
        }
    }

    fun getOrderByTitle(): Flow<Boolean> {
        return settingsDataStore.data.map { preferences ->
            preferences[KEY_ORDERBYTITLE] ?: false
        }
    }

    suspend fun setOrderByDesc(
        orderByDesc: Boolean
    ) {
        settingsDataStore.edit { preferences ->
            preferences[KEY_ORDERBYDESC] = orderByDesc
        }
    }

    fun getOrderByDesc(): Flow<Boolean> {
        return settingsDataStore.data.map { preferences ->
            preferences[KEY_ORDERBYDESC] ?: false
        }
    }


}