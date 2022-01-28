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

    val recordMonth = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading recordMonthFlow", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val recordMonth = preferences[StatisticsRepositoryKeys.RECORD_MONTH] ?: "month"
        recordMonth
    }


    val recordYear = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading recordYearFlow", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val recordYear = preferences[StatisticsRepositoryKeys.RECORD_YEAR] ?: "year"
        recordYear
    }

    val recordName = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading recordNameFlow", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val recordName = preferences[StatisticsRepositoryKeys.RECORD_NAME] ?: "name"
        recordName
    }

    suspend fun saveRecordMonth(month: String) {
        dataStore.edit { preference ->
            preference[StatisticsRepositoryKeys.RECORD_MONTH] = month
        }
    }

    suspend fun saveRecordYear(date: String) {
        dataStore.edit { preference ->
            preference[StatisticsRepositoryKeys.RECORD_YEAR] = date
        }
    }

    suspend fun saveRecordName(name: String) {
        dataStore.edit { preference ->
            preference[StatisticsRepositoryKeys.RECORD_NAME] = name
        }
    }

    private object StatisticsRepositoryKeys {
        val RECORD_MONTH = preferencesKey<String>("record_month")
        val RECORD_YEAR = preferencesKey<String>("record_year")
        val RECORD_NAME = preferencesKey<String>("record_name")
    }

}