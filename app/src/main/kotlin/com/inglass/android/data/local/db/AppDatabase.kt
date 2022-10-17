package com.inglass.android.data.local.db

import android.content.Context
import androidx.room.*
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.ScanResult

@Database(
    entities = [
        ScanResult::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanResultsDao(): ScanResultsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "inGlass_common_db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
