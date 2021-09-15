package ru.artkolest.libraryapp.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import ru.artkolest.libraryapp.View.adapter.BooksAdapter
import ru.artkolest.libraryapp.model.Repository
import ru.artkolest.libraryapp.model.ViewModelFactory
import ru.artkolest.libraryapp.network.Api
import kotlinx.coroutines.flow.collect
import ru.artkolest.libraryapp.R
import ru.artkolest.libraryapp.View.adapter.Adapter
import ru.artkolest.libraryapp.db.BooksDatabase

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val booksAdapter = BooksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val api = Api()
        val db = BooksDatabase(this)
        val repository = Repository(api, db)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        initAdapter()
        loadData()
        refreshApp()
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getModelBooks().collectLatest { pageData->
                booksAdapter.submitData(pageData)
            }
        }

        lifecycleScope.launch {
            booksAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { recyclerView.scrollToPosition(0) }
        }
    }

    private fun initAdapter() {
        recyclerView.adapter = booksAdapter.withLoadStateHeaderAndFooter(
            header = Adapter{booksAdapter.retry()},
            footer = Adapter{booksAdapter.retry()}
        )
        booksAdapter.addLoadStateListener { loadState->
            progressbar.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
        }
    }

    private fun refreshApp() {

        swipeToRefresh.setOnRefreshListener {
            booksAdapter.refresh()
            swipeToRefresh.isRefreshing = false
            Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
        }
    }
}