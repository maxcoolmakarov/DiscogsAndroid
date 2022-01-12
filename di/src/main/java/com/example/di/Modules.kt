package com.example.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.data.datasource.FavoritesDao
import com.example.data.datasource.SearchDataSource
import com.example.data.repository.FavoritesDaoImpl
import com.example.data.repository.SearchRepositoryImpl
import com.example.data.utils.AuthInterceptor
import com.example.domain.repository.SearchRepository
import com.example.presentation.viewmodel.ArtistViewModel
import com.example.presentation.viewmodel.SearchViewModel
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.discogs.com/"

class Modules {
    companion object {
        val viewModelModule = module {
            viewModel { SearchViewModel(get()) }
            viewModel { ArtistViewModel(get()) }
        }

        val apiModule = module {
            fun provideSearchDataSource(retrofit: Retrofit): SearchDataSource {
                return retrofit.create(SearchDataSource::class.java)
            }

            single { provideSearchDataSource(get()) }
        }

        val repositoryModule = module {
            fun provideSearchRepository(dataSource: SearchDataSource): SearchRepository =
                SearchRepositoryImpl(dataSource)

            single { provideSearchRepository(get()) }
        }

        val netModule = module {

            fun provideHttpClient(): OkHttpClient {
                val okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(AuthInterceptor()).addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                return okHttpClientBuilder.build()
            }

            fun provideGson(): Gson {
                return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
            }

            fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .client(client)
                    .build()
            }

            single { provideHttpClient() }
            single { provideRetrofit(get(), get()) }
            single { provideGson() }

        }

        val sharedPrefModule = module {
            fun provideSharedPref(app: Application): SharedPreferences {
                return app.applicationContext.getSharedPreferences(
                    "FAVORITES_KEY",
                    Context.MODE_PRIVATE
                )
            }
            single { provideSharedPref(androidApplication()) }
        }

        val favoriteModule = module {
            fun bindFavoritesDao(sharedPreferences: SharedPreferences): FavoritesDao {
                return FavoritesDaoImpl(sharedPreferences)
            }
            single { bindFavoritesDao(get()) }
        }
    }
}