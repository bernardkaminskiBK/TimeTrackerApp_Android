package com.berni.timetrackerapp.ui.fragments.records

import android.content.Context
import android.util.Log
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val PREFERENCE_NAME = "records_repository"
private const val TAG = "RecordsRepository"

class RecordsRepository(context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(PREFERENCE_NAME)

    val recordsTitle = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.e(TAG, "Error reading recordTitleFlow", exception)
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val recordName = preferences[StatisticsRepositoryKeys.TITLE_OF_RECORD] ?: "Records"
        recordName
    }

    suspend fun saveTitleRecord(title: String) {
        dataStore.edit { preference ->
            preference[StatisticsRepositoryKeys.TITLE_OF_RECORD] = title
        }
    }

    private object StatisticsRepositoryKeys {
        val TITLE_OF_RECORD = preferencesKey<String>("title_of_record")
    }

}