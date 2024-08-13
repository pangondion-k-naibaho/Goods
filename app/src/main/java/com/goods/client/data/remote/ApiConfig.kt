package com.goods.client.data.remote

import com.goods.client.data.Constants.URL_CONSTANTS.Companion.API_URL
import de.hdodenhof.circleimageview.BuildConfig
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun createApiService(): ApiService{
            val loggingInterceptor = if(BuildConfig.DEBUG){
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            }else{
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

//            val clientId = ""
//            val clientSecret = ""
//
//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor { chain ->
//                    val request = chain.request().newBuilder()
//                        .addHeader("Authorization", Credentials.basic(clientId, clientSecret))
//                        .build()
//                    chain.proceed(request)
//                }
//                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient())
//                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}