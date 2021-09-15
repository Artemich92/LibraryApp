package ru.artkolest.libraryapp.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.artkolest.booksapp.data.entities.Book
import ru.artkolest.libraryapp.data.RemoteKey
import ru.artkolest.libraryapp.network.Api
import java.io.IOException

@ExperimentalPagingApi
class BooksRemoteMediator (
    private val database: BooksDatabase,
    private val networkService: Api
) : RemoteMediator<Int, Book>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Book>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        return try {
            val response = networkService.getAllBooks(page)
            val isEndOfList = response.books.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.booksDao().clearAll()
                    database.remoteDao().deleteByQuery()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.books.map {
                    RemoteKey(it.isbn, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteDao().insertOrReplace(keys)
                database.booksDao().insertAll(response.books)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Book>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Book>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.isbn?.let { repoId ->
                database.remoteDao().remoteKeyByQuery(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Book>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { book -> database.remoteDao().remoteKeyByQuery(book.isbn) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Book>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat -> database.remoteDao().remoteKeyByQuery(cat.isbn) }
    }
}