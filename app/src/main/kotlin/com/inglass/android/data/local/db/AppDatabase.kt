package com.inglass.android.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inglass.android.data.local.db.dao.CompanionsDao
import com.inglass.android.data.local.db.dao.EmployeeDao
import com.inglass.android.data.local.db.dao.OperationsDao
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.Companions
import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.Operation
import com.inglass.android.data.local.db.entities.ScanResult

@Database(
    entities = [
        ScanResult::class,
        Employee::class,
        Operation::class,
        Companions::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanResultsDao(): ScanResultsDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun operationsDao(): OperationsDao
    abstract fun companionDao(): CompanionsDao

    companion object {

        fun getInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "inGlass_common_db"
            )
                .allowMainThreadQueries()
                .build()
        }
    }
}
