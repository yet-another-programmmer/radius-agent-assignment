package com.example.radiusagentassignment.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.radiusagentassignment.models.MyJsonServerResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.io.IOException

const val BASE_URL = "https://my-json-server.typicode.com"

private val moshi = Moshi
    .Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit
    .Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MyService {

    @GET("/iranjith4/ad-assignment/db")
    suspend fun index(): MyJsonServerResponse
}

object MyJsonServerApi {

    val myService: MyService by lazy {
        retrofit.create(MyService::class.java)
    }
}

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    ResultWrapper.GenericError(code, throwable)
                }
                else -> {
                    ResultWrapper.GenericError(null, null)
                }
            }
        }
    }
}
