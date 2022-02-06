package com.berni.timetrackerapp.api.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.berni.timetrackerapp.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE = 1
private const val PER_PAGE = 1

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
        ).liveData

    suspend fun getSearchResult(query: String) = unsplashApi.searchPhoto(query, PAGE, PER_PAGE)
}