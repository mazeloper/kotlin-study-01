package com.jschoi.develop.aop_part03_chapter04

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jschoi.develop.aop_part03_chapter04.dao.HistoryDao
import com.jschoi.develop.aop_part03_chapter04.dao.ReviewDao
import com.jschoi.develop.aop_part03_chapter04.model.History
import com.jschoi.develop.aop_part03_chapter04.model.Review

@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

     abstract fun reviewDao(): ReviewDao

}

fun getAppDatabase(context: Context): AppDatabase {

//     // DB버전이 업데이트 되었으면 해당 소스 같이 추가가 필요함
//     val migration_1_2 = object : Migration(1, 2) {
//         override fun migrate(database: SupportSQLiteDatabase) {
//             database.execSQL("CREATE TABLE `REVIEW` (`id` INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
//         }
//     }
    return Room.databaseBuilder(context, AppDatabase::class.java, "BookSearchDatabase")
        /*.addMigrations(migration_1_2)*/
        .build()
}