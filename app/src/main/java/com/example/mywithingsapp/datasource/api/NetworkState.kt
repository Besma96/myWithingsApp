package com.example.mywithingsapp.datasource.api

sealed class NetworkState<T> {
    class Success<T> : NetworkState<T>()
    class Loading<T> : NetworkState<T>()
    class Error<T> : NetworkState<T>()
}
