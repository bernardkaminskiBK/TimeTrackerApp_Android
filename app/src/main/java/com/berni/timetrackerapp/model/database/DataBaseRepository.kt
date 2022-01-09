package com.berni.timetrackerapp.model.database

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES = "user_preferences"
private const val TAG = "PreferencesManager"

enum class FilterOrder { BY_NAME, SHOW_ALL }

data class FilterPreferences(val filterQuery: String, val filterOrder: FilterOrder)

class PreferencesManager(context: Context) {

    private val datastore = context.createDataStore(USER_PREFERENCES)

    val preferencesFlow = datastore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val filterQuery = preferences[PreferencesKeys.FILTER_QUERY] ?: "Records"
            val filterOrder = FilterOrder.valueOf(
                preferences[PreferencesKeys.FILTER_ORDER] ?: FilterOrder.SHOW_ALL.name
            )
            FilterPreferences(filterQuery, filterOrder)
        }

    suspend fun updateFilterOrder(filterOrder: FilterOrder) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.FILTER_ORDER] = filterOrder.name
        }
    }

    suspend fun updateFilterQuery(filterQuery: String) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.FILTER_QUERY] = filterQuery
        }
    }

    private object PreferencesKeys {
        val FILTER_ORDER = preferencesKey<String>("filter_order")
        val FILTER_QUERY = preferencesKey<String>("filter_query")
    }

}