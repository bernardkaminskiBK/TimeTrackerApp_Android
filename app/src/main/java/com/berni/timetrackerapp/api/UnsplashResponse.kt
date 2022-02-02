package com.berni.timetrackerapp.api

import com.berni.timetrackerapp.model.entities.UnsplashPhoto


data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)
