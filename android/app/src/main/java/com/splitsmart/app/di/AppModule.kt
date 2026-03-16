package com.splitsmart.app.di

import android.content.Context
import com.splitsmart.app.BuildConfig
import com.splitsmart.app.data.api.ApiClient
import com.splitsmart.app.data.api.PersistentCookieJar
import com.splitsmart.app.data.api.SplitSmartApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun provideCookieJar(@ApplicationContext context: Context) = PersistentCookieJar(context)

    @Provides @Singleton
    fun provideMoshi(): Moshi = ApiClient.createMoshi()

    @Provides @Singleton
    fun provideOkHttpClient(cookieJar: PersistentCookieJar): OkHttpClient = ApiClient.createOkHttpClient(cookieJar)

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = ApiClient.createRetrofit(BuildConfig.BASE_URL + "/", client, moshi)

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): SplitSmartApi = ApiClient.createApi(retrofit)
}
