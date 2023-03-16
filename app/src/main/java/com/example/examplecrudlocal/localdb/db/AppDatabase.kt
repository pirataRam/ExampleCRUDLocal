package com.example.examplecrudlocal.localdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.examplecrudlocal.localdb.dao.PersonaDao
import com.example.examplecrudlocal.localdb.entities.Persona
import com.example.examplecrudlocal.tools.APP_DATABASE

@Database(entities = [Persona::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            val instanceExisting = INSTANCE
            if (instanceExisting != null) {
                return instanceExisting
            }
            synchronized(this) {
                val instanceNew = Room.databaseBuilder(
                    context = context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATABASE,
                ).build()
                INSTANCE = instanceNew
                return instanceNew
            }
        }
    }
}