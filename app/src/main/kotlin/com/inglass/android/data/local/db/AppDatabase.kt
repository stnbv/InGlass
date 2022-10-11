package app.inglass.tasker.data.db

import android.content.Context
import androidx.room.*
import com.inglass.android.data.local.db.DbConverters
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.ScanResult

@Database(
    entities = [
        ScanResult::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DbConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanResultsDao(): ScanResultsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "inglass_common_db"
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
//        private val MIGRATION_4_8: Migration = object : Migration(4, 8) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS `accumulativediscountonstart` (`spec_id` INTEGER NOT NULL, `current_discount` INTEGER, `next_month_discount` INTEGER, `next_month_discount_sum` REAL, PRIMARY KEY(`spec_id`))")
//            }
//        }
//
//        private val MIGRATION_7_8: Migration = object : Migration(7, 8) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS `accumulativediscountonstart` (`spec_id` INTEGER NOT NULL, `current_discount` INTEGER, `next_month_discount` INTEGER, `next_month_discount_sum` REAL, PRIMARY KEY(`spec_id`))")
//            }
//        }
    }
}
