package com.example.radiusagentassignment.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update

@Entity
data class RemoteConfig(
    @PrimaryKey val key: String,
    val value: String,
    val time: Long
)

@Dao
interface RemoteConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(config: RemoteConfig)

    @Delete
    fun delete(config: RemoteConfig)

    @Update
    fun update(config: RemoteConfig)

    @Query("SELECT * FROM remoteConfig WHERE key = :key")
    fun getConfig(key: String): RemoteConfig

    @Query("SELECT EXISTS(SELECT * FROM remoteconfig WHERE key = :key)")
    fun keyExists(key: String): Boolean
}

@Database(entities = [RemoteConfig::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun remoteConfigDao(): RemoteConfigDao
}