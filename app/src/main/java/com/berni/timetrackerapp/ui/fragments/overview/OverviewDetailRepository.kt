package com.berni.timetrackerapp.ui.fragments.overview

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val PREFERENCE_NAME = "overview_detail_repository"
private const val TAG = "OverviewDetailRepo"

class OverviewDetailRepository(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(PREFERENCE_NAME)

    val filterMonth = dataStore.data.catch { exception ->
        if(exception is IOException) {
            Log.e(TAG, "Error reading overview detail month value", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val month = preferences[OverviewDetailRepositoryKeys.MONTH] ?: "month"
        month
    }

    suspend fun saveMonthToDataStore(month: String) {
        dataStore.edit { preference ->
            preference[OverviewDetailRepositoryKeys.MONTH] = month
        }
    }

    val filterYear = dataStore.data.catch { exception ->
        if(exception is IOException) {
            Log.e(TAG, "Error reading overview detail year value", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val year = preferences[OverviewDetailRepositoryKeys.YEAR] ?: "year"
        year
    }

    suspend fun saveYearToDataStore(year: String) {
        dataStore.edit { preference ->
            preference[OverviewDetailRepositoryKeys.YEAR] = year
        }
    }

    private object OverviewDetailRepositoryKeys {
        val MONTH = preferencesKey<String>("month")
        val YEAR = preferencesKey<String>("year")
    }

}