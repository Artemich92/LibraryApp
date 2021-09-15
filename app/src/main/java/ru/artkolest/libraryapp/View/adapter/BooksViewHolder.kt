package ru.artkolest.libraryapp.View.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_books.view.*
import ru.artkolest.booksapp.data.entities.Book
import ru.artkolest.libraryapp.R

class BooksViewHolder(view: View): RecyclerView.ViewHolder(view) {

    fun bind(news:Book?){
        if (news == null) {

        }else{
            showData(news)
        }
    }

    private fun showData(book: Book) {
        itemView.tvTitle.text = book.title
        itemView.tvAuthor.text = book.author
        itemView.tvDescription.text = book.description
    }

    companion object{
        fun create(parent: ViewGroup) : BooksViewHolder{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_books, parent,false)
            return BooksViewHolder(view)
        }
    }
}