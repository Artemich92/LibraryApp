package ru.artkolest.libraryapp.View.adapter

import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.artkolest.booksapp.data.entities.Book
import ru.artkolest.libraryapp.R

class BooksAdapter : PagingDataAdapter<Book, RecyclerView.ViewHolder>(REPO_COMPARATOR) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val news = getItem(position)
        holder.itemView.animation =
            AnimationUtils.loadAnimation (holder.itemView. context , R.anim.anim_resourse_file)
        if(news != null){
            (holder as BooksViewHolder).bind(news)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BooksViewHolder.create(parent)
    }

    companion object{
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Book>(){
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.isbn == newItem.isbn
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

        }
    }

}