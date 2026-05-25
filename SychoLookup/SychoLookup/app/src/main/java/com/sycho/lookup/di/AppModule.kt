package com.sycho.lookup.di

import android.content.Context
import androidx.room.Room
import com.sycho.lookup.data.local.SearchHistoryDao
import com.sycho.lookup.data.local.SearchHistoryDatabase
import com.sycho.lookup.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://sychosimdatabase.vercel.app/api/"
    private const val DATABASE_NAME = "sycho_lookup_db"

    @Provides @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SearchHistoryDatabase =
        Room.databaseBuilder(context, SearchHistoryDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides @Singleton
    fun provideSearchHistoryDao(database: SearchHistoryDatabase): SearchHistoryDao =
        database.searchHistoryDao()
}
