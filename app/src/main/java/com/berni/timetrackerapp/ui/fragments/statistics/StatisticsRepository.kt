package com.berni.timetrackerapp.ui.fragments.statistics

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val PREFERENCE_NAME = "statistics_repository"
private const val TAG = "StatisticsRepository"

class StatisticsRepository(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(PREFERENCE_NAME)

    val recordName = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading recordNameFlow", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val recordName = preferences[StatisticsRepositoryKeys.NAME_OF_RECORD] ?: "name"
        recordName
    }

    val recordDate = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading recordDateFlow", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val recordDate = preferences[StatisticsRepositoryKeys.DATE_OF_RECORD] ?: "date"
        recordDate
    }

    suspend fun saveNameOfRecord(name: String) {
        dataStore.edit { preference ->
            preference[StatisticsRepositoryKeys.NAME_OF_RECORD] = name
        }
    }

    suspend fun savaDateOfRecord(date: String) {
        dataStore.edit { preference ->
            preference[StatisticsRepositoryKeys.DATE_OF_RECORD] = date
        }
    }

    private object StatisticsRepositoryKeys {
        val NAME_OF_RECORD = preferencesKey<String>("name_of_record")
        val DATE_OF_RECORD = preferencesKey<String>("date_of_record")
    }

}