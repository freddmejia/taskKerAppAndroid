package gamer.botixone.mytasks.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gamer.botixone.mytasks.data.local.dao.TaskDao
import gamer.botixone.mytasks.data.local.model.TaskEntity

@Database(entities =
[
    TaskEntity::class,
],
    version = 1, exportSchema = false
)

abstract  class DataModelDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var instance: DataModelDatabase? = null
        private const val DB_NAME = "taskKer.db"

        fun getDatabase(context: Context): DataModelDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, DataModelDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

    }
}