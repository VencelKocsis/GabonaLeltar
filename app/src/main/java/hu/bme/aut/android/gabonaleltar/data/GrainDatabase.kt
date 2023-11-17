package hu.bme.aut.android.gabonaleltar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter

@Database(entities = [GrainItem::class, TransactionItem::class], version = 2)
abstract class GrainDatabase : RoomDatabase() {
    abstract fun grainDao(): GrainItemDAO
    abstract fun transactionDao(): TransactionItemDAO

    companion object {
        private var INSTANCE: GrainDatabase? = null
        fun getInstance(context: Context): GrainDatabase {
            if (INSTANCE == null) {
                synchronized(GrainDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GrainDatabase::class.java,
                        "app-database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}