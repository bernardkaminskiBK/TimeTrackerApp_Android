package com.berni.timetrackerapp.model.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.berni.timetrackerapp.model.entities.Record
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TimeTrackerDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TimeTrackerRoomDatabase
    private lateinit var dao: TimeTrackerDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TimeTrackerRoomDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.timeTrackerDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertRecord() = runBlockingTest {
        val timeTrackerRecord = Record(id = 1, 5000000L, "Duolingo", "00:45:00")
        dao.insertRecord(timeTrackerRecord)

        val allRecords = dao.getRecordsList().first()
        assertThat(allRecords).contains(timeTrackerRecord)
    }

    @Test
    fun testDeleteRecord() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val deleteRecord =  Record(id = 4, 250000L, "Work", "03:00:00")
        dao.deleteRecord(deleteRecord)

        val allRecords = dao.getRecordsList().first()
        assertThat(allRecords).doesNotContain(deleteRecord)
    }

    @Test
    fun testDeleteAllRecords() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        dao.deleteAllRecords()

        val allRecords = dao.getRecordsList().first()
        assertThat(allRecords.isEmpty()).isTrue()
    }

    @Test
    fun testUpdateRecord() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val updateRecord = Record(id = 3, 250000L, "Running", "03:00:00")
        dao.updateRecord(updateRecord)

        val allRecords = dao.getRecordsList().first()
        assertThat(allRecords).contains(updateRecord)
    }

    @Test
    fun testGetAllRecordsWithoutDuplicates() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val allRecordsWithoutDuplicates = dao.getAllRecordNames().first()
        val listOfExpectedRecordNames = listOf("Duolingo", "Work", "Cooking")

        assertThat(allRecordsWithoutDuplicates).isEqualTo(listOfExpectedRecordNames)
    }

    @Test
    fun testGetFilteredRecordsListByName() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val allFilteredRecordNames = dao.getFilteredRecordsListByNameByDate("Cooking").first()
        val listOfExpectedFilteredRecordNames = RecordTestData.listOfRecordsFilteredByName()

        assertThat(allFilteredRecordNames).isEqualTo(listOfExpectedFilteredRecordNames)
    }

    @Test
    fun testGetAllRecordsList() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val allRecordsList = dao.getRecordsList().first()
        val expectedListOfAllRecords =  RecordTestData.listOfRecords()

        assertThat(allRecordsList).isEqualTo(expectedListOfAllRecords)
    }

    @Test
    fun testGetRecordsShowAllRecords() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val allRecordsList = dao.getRecords("", FilterOrder.SHOW_ALL).first()
        val expectedListOfAllRecords =  RecordTestData.listOfRecords()

        assertThat(allRecordsList).isEqualTo(expectedListOfAllRecords)
    }

    @Test
    fun testGetRecordsShowAllRecordsByName() = runBlockingTest {
        RecordTestData.listOfRecords().asSequence().iterator().forEach {
            dao.insertRecord(it)
        }

        val filteredRecordsListByName = dao.getRecords("Cooking", FilterOrder.BY_NAME).first()
        val expectedFilteredListOfAllRecordNames =  RecordTestData.listOfRecordsFilteredByName()

        assertThat(filteredRecordsListByName).isEqualTo(expectedFilteredListOfAllRecordNames)
    }

}