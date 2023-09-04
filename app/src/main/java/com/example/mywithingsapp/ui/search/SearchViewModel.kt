package com.example.mywithingsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.mywithingsapp.commons.base.BaseViewModel
import com.example.mywithingsapp.commons.utils.Constants.Companion.PER_PAGE
import com.example.mywithingsapp.datasource.api.NetworkState
import com.example.mywithingsapp.datasource.pagination.PixabayDataSourceFactory
import com.example.mywithingsapp.domain.entities.Image
import com.example.mywithingsapp.domain.repository.PixabayRepository

class SearchViewModel(pixabayRepository: PixabayRepository) : BaseViewModel() {

    // FOR DATA ---
    private val pixabayDataSource =
        PixabayDataSourceFactory(pixabayRepository = pixabayRepository, scope = ioScope)

    // OBSERVABLES ---
    val pixabayData: LiveData<PagedList<Image>> =
        LivePagedListBuilder(pixabayDataSource.sortById(), pagedListConfig()).build()

    val networkState: LiveData<NetworkState<Int>>? =
      pixabayDataSource.dataSource.switchMap { it.getNetworkState() }

    // PUBLIC API ---

    /**
     * Fetch a list of [Image] by Query
     */
    fun fetchKeyQuery(query: String) {
        if (pixabayDataSource.getQuery() == query.trim()) return
        pixabayDataSource.updateQuery(query.trim())
    }

    // UTILS ---
    private fun pagedListConfig() = PagedList.Config.Builder().setPageSize(PER_PAGE).build()

    /** sorts HitsList by ID in descending order */
    private fun DataSource.Factory<Int, Image>.sortById(): DataSource.Factory<Int, Image> {
        return mapByPage {
            it.sortedWith(compareByDescending { item -> item.id })
        }
    }
}
