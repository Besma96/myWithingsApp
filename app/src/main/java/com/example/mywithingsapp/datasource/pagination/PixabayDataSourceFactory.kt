package com.example.mywithingsapp.datasource.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.mywithingsapp.domain.entities.Image
import com.example.mywithingsapp.domain.repository.PixabayRepository
import kotlinx.coroutines.CoroutineScope


class PixabayDataSourceFactory(
    private val pixabayRepository: PixabayRepository,
    private val scope: CoroutineScope,
    private var query: String = ""
) : DataSource.Factory<Int, Image>() {
    val dataSource = MutableLiveData<PixabayDataSource>()

    override fun create(): DataSource<Int, Image> {

        val dataSource = PixabayDataSource(pixabayRepository, scope, query)
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    // --- PUBLIC API
    fun getQuery() = query

    private fun getSource() = dataSource.value

    fun updateQuery(query: String) {
        this.query = query
        getSource()?.refresh()
    }
}
