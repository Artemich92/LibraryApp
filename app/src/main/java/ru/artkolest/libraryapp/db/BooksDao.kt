package ru.artkolest.libraryapp.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.artkolest.booksapp.data.entities.Book

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<Book>)

    @Query("SELECT * FROM books")
    fun pagingSource(): PagingSource<Int, Book>

    @Query("DELETE FROM books")
    suspend fun clearAll()
}