package com.sitramobile.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sitramobile.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiClient {

    companion object {

        operator fun invoke(): ApiInterface {
            val gson = Gson()
            val retrofitBuilder: Retrofit.Builder by lazy {
                Retrofit.Builder().client(
                    OkHttpClient.Builder()
                        .connectTimeout(3, TimeUnit.MINUTES) // connect timeout
                        .writeTimeout(3, TimeUnit.MINUTES) // write timeout
                        .readTimeout(2, TimeUnit.MINUTES) // read timeout
                        .also { client ->
                            if (BuildConfig.DEBUG) {
                                val logging = HttpLoggingInterceptor()
                                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                                client.addInterceptor(logging)
                            }
                        }.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())

            }

            val apiservice: ApiInterface by lazy {
                retrofitBuilder.build().create(ApiInterface::class.java)
            }
            return apiservice
        }

        const val BASE_URL = "http://lab.sitraonline.org.in/energyauditapi/index.php/api/"
        // 148.72.214.201 this IP for above url
    }
}