package com.example.mywithingsapp.datasource.pagination

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.mywithingsapp.commons.utils.Constants
import com.example.mywithingsapp.datasource.api.NetworkState
import com.example.mywithingsapp.domain.entities.Image
import com.example.mywithingsapp.domain.repository.PixabayRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PixabayDataSource(
    private val pixabayRepository: PixabayRepository, private val scope: CoroutineScope,
    private val query: String
) : PageKeyedDataSource<Int, Image>() {

    // FOR DATA ---
    private val networkState = MutableLiveData<NetworkState<Int>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Image>
    ) {
        executeQuery(1) { callback.onResult(it, null, 2) }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {
        executeQuery(params.key) { callback.onResult(it, params.key + 1) }
    }

    // UTILS ---
    private fun executeQuery(page: Int, callback: (List<Image>) -> Unit) {
        networkState.postValue(NetworkState.Loading())
        scope.launch {
            try {
                val response = pixabayRepository.fetchPixabayData(query, page)
                response.let {
                    if (response.isSuccessful) {
                        response.body()?.hits?.let { data -> callback(data) }
                        networkState.postValue(NetworkState.Success())
                    } else networkState.postValue(NetworkState.Error())
                }
            } catch (exception: HttpException) {
                networkState.postValue(NetworkState.Error())
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Image>) {
        /** To Load the Data in Pull to refresh **/
    }

    // PUBLIC API ---
    fun refresh() = this.invalidate()
    fun getNetworkState(): LiveData<NetworkState<Int>> = networkState
}
