package com.splitsmart.app.data.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "splitsmart_prefs")

object CookieKeys {
    val COOKIES = stringSetPreferencesKey("cookies")
}

class PersistentCookieJar(private val context: Context) : CookieJar {
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val cookieStrings = cookies.map { it.toString() }.toSet()
        runBlocking {
            context.dataStore.edit { prefs ->
                val existing = prefs[CookieKeys.COOKIES] ?: emptySet()
                prefs[CookieKeys.COOKIES] = existing + cookieStrings
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return runBlocking {
            context.dataStore.data.map { prefs ->
                val cookieStrings = prefs[CookieKeys.COOKIES] ?: emptySet()
                cookieStrings.mapNotNull { Cookie.parse(url, it) }
            }.first()
        }
    }

    suspend fun clearCookies() {
        context.dataStore.edit { prefs -> prefs.remove(CookieKeys.COOKIES) }
    }
}

object ApiClient {
    fun createMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    fun createOkHttpClient(cookieJar: PersistentCookieJar): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun createRetrofit(baseUrl: String, client: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
    }

    fun createApi(retrofit: Retrofit): SplitSmartApi = retrofit.create(SplitSmartApi::class.java)
}
