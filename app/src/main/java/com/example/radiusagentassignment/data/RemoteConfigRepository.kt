package com.example.radiusagentassignment.data

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.radiusagentassignment.models.MyJsonServerResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(myDatabase: MyDatabase) {
    val remoteConfigDao = myDatabase.remoteConfigDao()
    val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter: JsonAdapter<MyJsonServerResponse> =
        moshi.adapter(MyJsonServerResponse::class.java)

    fun getResponse(): MyJsonServerResponse? {
        if (!remoteConfigDao.keyExists(KEY)) {
            return null
        }
        val config = remoteConfigDao.getConfig(KEY)
        if (System.currentTimeMillis() - config.time < TimeUnit.MINUTES.toMillis(1)) {
            return jsonAdapter.fromJson(config.value)
        }
        return null
    }

    fun setResponse(response: MyJsonServerResponse) {
        val config = RemoteConfig(
                KEY,
                jsonAdapter.toJson(response),
                System.currentTimeMillis()
        )
        remoteConfigDao.insert(config)
    }

    companion object {
        const val KEY = "FACILITIES_EXCLUSIONS"
    }
}