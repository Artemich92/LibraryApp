package ru.artkolest.booksapp.data.entities

import com.google.gson.annotations.SerializedName

data class ResponceEntity(
    val context: String,
    val id: String,
    val type: String,
    @SerializedName("hydra:member")
    val books: List<Book>,
    @SerializedName("hydra:totalItems")
    val hydraTotalItems: Int,
)