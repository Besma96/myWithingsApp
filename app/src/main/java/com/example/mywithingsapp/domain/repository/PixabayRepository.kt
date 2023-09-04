package com.example.mywithingsapp.domain.repository

import com.example.mywithingsapp.BuildConfig
import com.example.mywithingsapp.commons.utils.Constants.Companion.PER_PAGE
import com.example.mywithingsapp.datasource.api.PixabayApi

class PixabayRepository(private val pixabayApi: PixabayApi) {

    suspend fun fetchPixabayData(key: String, page: Int) =
        pixabayApi.getPixabayDataAsync("18021445-326cf5bcd3658777a9d22df6f", key, page, PER_PAGE).await()
}
