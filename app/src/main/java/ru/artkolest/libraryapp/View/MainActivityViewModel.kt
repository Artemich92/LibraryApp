package ru.artkolest.libraryapp.View

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.artkolest.booksapp.data.entities.Book
import ru.artkolest.libraryapp.model.Repository

@ExperimentalPagingApi
class MainActivityViewModel (val repository: Repository): ViewModel() {

    val bookResult: Flow<PagingData<Book>> = repository.getResult().cachedIn(viewModelScope)

    fun getModelBooks(): Flow<PagingData<Book>> {
        return bookResult
    }
}