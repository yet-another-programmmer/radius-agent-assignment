package com.example.radiusagentassignment

import android.content.Context
import androidx.room.Room
import com.example.radiusagentassignment.data.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object HiltModules {

    @Provides
    fun provideMydatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(context, MyDatabase::class.java, "my_database").build()
    }
}