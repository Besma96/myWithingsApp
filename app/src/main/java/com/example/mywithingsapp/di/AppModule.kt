package com.example.mywithingsapp.di

import com.example.mywithingsapp.BuildConfig
import com.example.mywithingsapp.commons.utils.UiHelper
import com.example.mywithingsapp.datasource.api.PixabayApi
import com.example.mywithingsapp.datasource.api.createNetworkClient
import com.example.mywithingsapp.domain.repository.PixabayRepository
import com.example.mywithingsapp.ui.search.SearchViewModel
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.Retrofit
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
}

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            viewModelModule,
            repositoryModule,
            networkModule,
            uiHelperModule
        )
    )
}

val viewModelModule = module {
    viewModel { SearchViewModel(pixabayRepository = get()) }
}

val repositoryModule = module {
    single { PixabayRepository(pixabayApi = get()) }
}

val networkModule = module {
    single { pixabayApi }
}

val uiHelperModule = module {
    single { UiHelper(androidContext()) }
}

private val retrofit: Retrofit = createNetworkClient(BuildConfig.BASE_URL)

private val pixabayApi: PixabayApi = retrofit.create(PixabayApi::class.java)
