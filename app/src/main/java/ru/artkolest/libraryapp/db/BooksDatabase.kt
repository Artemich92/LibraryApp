package ru.artkolest.libraryapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.artkolest.booksapp.data.entities.Book
import ru.artkolest.libraryapp.data.RemoteKey

@Database(
    entities = [Book::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class BooksDatabase : RoomDatabase(){

    abstract fun booksDao():BooksDao
    abstract fun remoteDao():RemoteDao

    companion object{
        @Volatile private var instance : BooksDatabase? = null
        private val lock = Any()

        operator fun invoke(context: Context) = instance?: synchronized(lock){
            instance?:buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            BooksDatabase::class.java,
            "news"
        ).build()
    }
}