package ru.artkolest.booksapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "books")
data class Book(
    @SerializedName("@id")
    val id: String,
    @SerializedName("@type")
    val type: String,
    val author: String,
    val description: String,
    @PrimaryKey
    val isbn: String,
    val publicationDate: String,
    val title: String
)