package ru.artkolest.libraryapp.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.artkolest.booksapp.data.entities.Book
import ru.artkolest.libraryapp.db.BooksDatabase
import ru.artkolest.libraryapp.db.BooksRemoteMediator
import ru.artkolest.libraryapp.network.Api

@ExperimentalPagingApi
class Repository (private val api : Api, private val db: BooksDatabase) {

    fun getResult(): Flow<PagingData<Book>>{
        val pagingSourceFactory = { db.booksDao().pagingSource() }
        return Pager(
            config = PagingConfig(pageSize = 5,enablePlaceholders = false),
            remoteMediator = BooksRemoteMediator(db,api),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}