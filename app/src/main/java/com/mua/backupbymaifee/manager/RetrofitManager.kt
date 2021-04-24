package com.mua.backupbymaifee.manager

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mua.backupbymaifee.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitManager {

    companion object {
        //private val logEnabled: Boolean = BuildConfig.DEBUG
        private val logEnabled = false
        private const val TAG = "retrofit-mua"
        fun newInstance(): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            var baseUrl = "http://192.168.0.111:8080"
            baseUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(2 * 60.toLong(), TimeUnit.SECONDS)
            httpClient.readTimeout(5 * 60.toLong(), TimeUnit.SECONDS)
            httpClient.writeTimeout(5 * 60.toLong(), TimeUnit.SECONDS)
            httpClient.addInterceptor { chain: Interceptor.Chain ->
                val request: Request = chain.request().newBuilder().addHeader(
                    "a", "b"
                ).build()
                chain.proceed(request)
            }
            if (logEnabled) {
                val loggingInterceptor = HttpLoggingInterceptor { message -> Log.d(TAG, message) }
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClient.addInterceptor(loggingInterceptor)
            }
            retrofitBuilder.client(httpClient.build())
            return retrofitBuilder.build()
        }
    }

}